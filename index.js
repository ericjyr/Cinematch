const express = require('express');
const app = express();
const http = require('http');
const { Server } = require('socket.io');
const cors = require('cors');

app.use(cors());

const server = http.createServer(app);
const io = new Server(server, {
    cors: {
        origin: 'http://localhost:3000',
        methods: ['GET', 'POST'],
    },
});

const rooms = {};
const connectedUsers = {};

//connected user
io.on('connection', (socket) => {
  connectedUsers[socket.id] = {};

  socket.on('sendFriendRequest', (request) => {
    const targetSocket = io.sockets.sockets.get(request.id);
    if (targetSocket) {
      targetSocket.emit('friendRequest', { ...request, id: socket.id });
    }
  });

  socket.on('acceptFriendRequest', (request) => {
    const targetSocket = io.sockets.sockets.get(request.id);
    if (targetSocket) {
      targetSocket.emit('friendRequestAccepted', { id: socket.id });
      socket.emit('friendRequestAccepted', { id: request.id });
    }
  });


  socket.on('rejectFriendRequest', (request) => {
    const targetSocket = io.sockets.sockets.get(request.id);
    if (targetSocket) {
      targetSocket.emit('friendRequestRejected', { id: socket.id });
      socket.emit('friendRequestRejected', { id: request.id });
    }
  });



  socket.on('create_room', ({room, userID}) => {
    rooms[room] = [];
    rooms[room].push({ id: socket.id, choice: undefined, userid: userID});
    console.log(rooms[room]);
    //User join the room
    socket.join(room);

    if (rooms[room].length !== 2) {
      // Send an error message to the client
      socket.emit('waiting', 'Waiting for other user');
      return;
    }

    //Check the number of user
    if (rooms[room].length === 2) {
      const users = rooms[room].map(user => ({ id: user.id, userID: user.userID }) );
      io.to(room).emit('room_ready', users);
    }
  });


// join room event, if room not exist, create a new room
  socket.on('join_room', ({ room, userID }) => {
    console.log(rooms[room]);
    // Check if the room exists
    if (!rooms[room]) {
      // Send an error message to the client
      socket.emit('error', 'The room does not exist');
      return;
    }

    if (rooms[room].length === 2) {
      socket.emit('error', 'The room is full');
      return;
    }

    rooms[room].push({ id: socket.id, choice: undefined, userid: userID });
    socket.join(room);

    if (rooms[room].length === 2) {
      const users = rooms[room].map(user => ({ id: user.id, userID: user.userID }));
      io.to(room).emit('room_ready', users);
    }

    console.log(rooms[room]);
  });


  function resetChoices(room) {
    rooms[room] = [];
  }

  socket.on('choose_movie', ({ likedCards, dislikedCards, maybeCards, room }) => {
    if (rooms[room]) {
      const user = rooms[room].find(user => user.id === socket.id);

      if (user) {
        user.choice = {
          likedCards: likedCards || [],
          dislikedCards: dislikedCards || [],
          maybeCards: maybeCards || [],
        };

        if (rooms[room].every(user => user.choice !== undefined)) {
          const choices = rooms[room].map(user => user.choice);
          const sortedCards = combineAndSortScores(choices);
          io.to(room).emit('match_result', sortedCards);
          resetChoices(room);
        }
      }
    }
  });


  function combineAndSortScores(choices) {
    // Initialize combinedScores array
    const combinedScores = [];

    // Iterate through each user's choices
    choices.forEach(userChoices => {
      // Iterate through liked, disliked, and maybe cards
      Object.keys(userChoices).forEach(cardType => {
        // Iterate through each card in the current card type
        userChoices[cardType].forEach(card => {
          const cardId = card.id;
          const score = getScoreByCardType(cardType); // Get the score based on the card type
          const existingCard = combinedScores.find(existing => existing.id === cardId);

          if (existingCard) {
            // Update existing card score
            existingCard.score += score;
          } else {
            // Add new card to combinedScores array
            combinedScores.push({
              id: cardId,
              title: card.title, // Include the title in the result
              poster: card.image,
              score: score,
              year: card.year,
            });
          }
        });
      });
    });


    // Convert combinedScores object to an array of objects
    return combinedScores.sort((a, b) => {
      if (b.score !== a.score) {
        return b.score - a.score; // Sort by combined score in descending order
      } else {
        return b.year - a.year; // Sort by movie year in descending order
      }
    });

  }

// Helper function to get score based on card type
  function getScoreByCardType(cardType) {
    // Assign scores based on card type (modify as needed)
    switch (cardType) {
      case 'likedCards':
        return 5; // Liked card score
      case 'dislikedCards':
        return 0; // Disliked card score
      case 'maybeCards':
        return 3; // Maybe card score
      default:
        return 0; // Default score
    }
  }


  socket.on('disconnect', () => {
    for (const room in rooms) {
        //Remove the current user(socket.id) form the list when trigger disconnect
      rooms[room] = rooms[room].filter(u => u.id !== socket.id);
      if (rooms[room].length === 0) {
        resetChoices(room);
        delete rooms[room];
      }
    }
  });


});

server.listen(3001, () => {
    console.log('SERVER IS RUNNING');
});
