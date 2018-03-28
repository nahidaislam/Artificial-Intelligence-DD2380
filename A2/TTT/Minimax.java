import java.util.*;

public class Minimax {


 public static int totalDepth;
 public static int lowerThreshold = 5;
 
 public static int x1, x2, x3, x4, o1, o2, o3, o4;

 public static int[][] diags = {{0, 5, 10, 15}, {3, 6, 9, 12}};
 public static int[][] rows = {{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}};
 public static int[][] cols = {{0, 4, 8, 12}, {1, 5, 9, 13}, {2, 6, 10, 14}, {3, 7, 11, 15}};
  
  
 public static void updateOsAndXs(int scoreRow, int player){

    if(scoreRow==4 && player == 2) o4++;
    if(scoreRow==3 && player == 2) o3++;
    if(scoreRow==2 && player == 2) o2++;
    if(scoreRow==1 && player == 2) o1++;
 
    if(scoreRow==4 && player == 1) x4++;
    if(scoreRow==3 && player == 1) x3++;
    if(scoreRow==2 && player == 1) x2++;
    if(scoreRow==1 && player == 1) x1++;
 
 } 
  
 public static void diags(final GameState gameState, int player){

	int diagScore = 0;
	int other = getOtherPlayer(player); 
	//  diagonals
	for(int d = 0; d < diags.length; d++){
		for(int index = 0; index < diags[0].length; index++){
			if(gameState.at(diags[d][index]) == other){
				diagScore = 0;
				break;
			}
			if(gameState.at(diags[d][index]) == player){
				diagScore++;
			}
		}
		updateOsAndXs(diagScore, player);
	}
 }

 public static void rows(final GameState gameState, int player){
 
 		// rows
 		int rowScore = 0;
 		int other = getOtherPlayer(player);
 		for(int r = 0; r < rows.length; r++){
 			for(int index = 0; index < rows[0].length; index++){
 				if(gameState.at(rows[r][index]) == other){
 					rowScore = 0;
 					break;
 				}
 				if(gameState.at(rows[r][index]) == player){
 					rowScore++;
 				}
			}
			
			updateOsAndXs(rowScore, player);
		}
 		
 }
 
 public static void cols(final GameState gameState, int player){
 
 		// cols
 		int colScore = 0;
 		int other = getOtherPlayer(player);
 		for(int r = 0; r < cols.length; r++){
 			for(int index = 0; index < cols[0].length; index++){
 				if(gameState.at(cols[r][index]) == other){
 					colScore = 0;
 					break;
 				}
 				if(gameState.at(cols[r][index]) == player){
 					colScore++;
 				}
			}
			
			updateOsAndXs(colScore, player);
		}
 }
  
 private static int calcDepth(final GameState gameState){
    int depth = 0;
    for(int index = 0; index < gameState.CELL_COUNT; index++){
        if(gameState.at(index) == 0)
        depth++;
    }
    return depth;
 }
 

 public static int getOtherPlayer(int player){
 
    int otherplayer = (player == 1? 2 : 1);
    
    return otherplayer;
 
 } 

 public static void zeroOsAndXs(){
 	o4 = 0; o3 = 0; o2 = 0; o1 = 0;
 	x4 = 0; x3 = 0; x2 = 0; x1 = 0;
 }

 public static int eval(final GameState gameState, int depth){
     
     zeroOsAndXs();
     diags(gameState, Constants.CELL_X);
     rows(gameState, Constants.CELL_X); 
     cols(gameState, Constants.CELL_X);
     
     diags(gameState, Constants.CELL_O);
     rows(gameState, Constants.CELL_O); 
     cols(gameState, Constants.CELL_O);
     
     int score = (100 * x4 + 10 * x3 + 3 * x2 + x1) - (100 * o4 + 10 * o3 + 3 * o2 + o1);

     score += depth;
     
     return score; 
 }



 /*
 function alphabetasearch(state) returns an action (in this case, the resulting game state of the action)
    v <- MAX_VALUE(state, NEGATIVE_INFINITY, INFINITY);
        return the action in ACTIONS(state) with value v
 ***/
 
 /* initialises alpha-beta search */
 public static GameState alphabeta(GameState gameState){

  Vector<GameState> nextStates = new Vector<GameState>();
  gameState.findPossibleMoves(nextStates);
 
  int depth = calcDepth(gameState);
  totalDepth = depth;
  int maxdepth = (depth <= lowerThreshold? 0 : (int) ((double)depth * 0.75));

  int alpha = Integer.MIN_VALUE;
  int beta = Integer.MAX_VALUE;
  
  int player = gameState.getNextPlayer();
  int bestVal = Integer.MIN_VALUE;
  int current = Integer.MIN_VALUE;

  int bestGameState = -1;
  for(int i = 0; i < nextStates.size(); i++){
     current = alphabeta(nextStates.elementAt(i), depth-1, alpha, beta, getOtherPlayer(player), maxdepth);
     
     if(current > bestVal){
         bestVal = current;
         bestGameState = i;
     }
  }
  
  return nextStates.elementAt(bestGameState);
 
 }

 /* mini-max search with alpha-beta pruning */
 public static int alphabeta(GameState gameState, int depth, int alpha, int beta, int player, int maxdepth){
 
//  System.err.println("at alphabeta depth " + depth + ". Current game state:");
//      System.err.println("DEPTH " + depth + " PLAYER " + player + " ALPHA " + alpha + " BETA " + beta);
    
    Vector<GameState> nextStates = new Vector<GameState>();
    gameState.findPossibleMoves(nextStates);
    
    int v;
    
        // if end-of-game
    if(depth == maxdepth || nextStates.size() == 0){
        v = eval(gameState, depth);
        // MAX's turn to play
    } else if(player == Constants.CELL_X){
        v = Integer.MIN_VALUE;
        for(int i = 0; i < nextStates.size(); i++){
            v = Math.max(v, alphabeta(nextStates.elementAt(i), depth-1, alpha, beta, Constants.CELL_O, (depth <= lowerThreshold? 0 : maxdepth)));
            alpha = Math.max(v, alpha);
            if(alpha >= beta){  // alpha pruning
                break;
            }
        }
        // MIN's turn to play
    } else {
        v = Integer.MAX_VALUE;
        for(int i = 0; i < nextStates.size(); i++){
            v = Math.min(v, alphabeta(nextStates.elementAt(i), depth-1, alpha, beta, Constants.CELL_X, (depth <= lowerThreshold? 0 : maxdepth)));
            beta = Math.min(v, beta);
            if(alpha >= beta){  // beta pruning
                break;
            }
        }
    }

    return v;
 }
 

}
