# Othello-AI-Project
Java creation of the Othello game with an Ai made for said game. Developed during AI class Fall 2024 Semester.
The created ai's class name is OthelloPlayerBoy. AI utilizes minimax with Alpha-Beta Prunning. Each turn, the ai has 5 seconds max to calculate the best move it can do that turn. For the heuristic function, each space (with the exception of the corners) with the current player's token is +1 point, while -1 for the opponent's token. The corners, are +5 for the current player and -20 for the opponents.

To run, either run the Othello class main function, or the OthelloTournament class main function (runs multiple games).
