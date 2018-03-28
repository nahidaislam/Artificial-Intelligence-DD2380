import java.util.*;
public class Search{

public static int totalDepth;
public static int lowerThreshold= 5;

public static int evalPart(final GameState gameState, int depth){

  //rows and columns
  int sum = 0;
  for (int row = 0; row < gameState.BOARD_SIZE; row++) {
    for (int col = 0; col < gameState.BOARD_SIZE; col++ ) {
      sum +=(gameState.at(row, col) == Constants.CELL_X? 1 : 0);
    }
  }

  //1st diagonal
  int diagSum = 0;
  //15 or 16?
  for (int index = 0; index <= 15; index +=5) {
    diagSum +=(gameState.at(index) == Constants.CELL_X? 1 : 0);
  }

  //2nd diagonal
  for (int index = 3; index <= 12; index +=3) {
    diagSum += (gameState.at(index) == Constants.CELL_X? 1 : 0);
  }
  int score = sum*2 + diagSum;
  return(score + depth);
}

public static int maxDiag (final GameState gameState, int player){

  //1st diagonal
  int scoreDiag1 = 0;
  for (int index = 0; index < 16; index +=5) {
    //scoreDiag1 += (gameState.at(index) == player? 1 : 0);
    if (gameState.at(index) != player) {
      scoreDiag1 += 0;
    }else{
      scoreDiag1 += 1;
    }
  }
  //2nd diagonal
  int scoreDiag2 = 0;
  for (int index =3; index < 13; index +=3) {
    scoreDiag2 += (gameState.at(index) == player? 1 : 0);
  }
  int maxDiag = Math.max (scoreDiag1, scoreDiag2);
  return maxDiag;
}

public static int maxRow (final GameState gameState, int player){
  //rows or index?
  int maxRow = 0;
  int scoreRow = 0;
  for (int row = 0; row < gameState.CELL_COUNT; row++) {
    // if (row%4 == 0) {
    //   if (scoreRow > maxRow) maxRow = scoreRow;
    //   scorerow = 0;
    // }
    //scoreRow += (gameState.at(row) == player? 1 : 0);
    if (gameState.at(row) != player) {
      scoreRow+= 0;
    }else{
      scoreRow += 1;
    }
    if (row%4 == 0) {
      if (scoreRow > maxRow) maxRow = scoreRow;
      scoreRow = 0;
    }
  }
  return maxRow;
}
public static int maxCol (final GameState gameState, int player){

  int maxCol = 0;
  int scoreCol = 0;
  for (int row = 0; row < 4; row++) {
    for (int col = row; col < gameState.CELL_COUNT; col +=4) {
        scoreCol += (gameState.at(col) == player? 1 : 0);
    }
    if (scoreCol > maxCol) maxCol = scoreCol;
    scoreCol= 0;
  }
  return maxCol;
}

public static int maxInaRow (final GameState gameState, int player){

  int maxRow = maxRow(gameState, player);
  int maxCol = maxCol(gameState, player);
  int maxDiag = maxDiag(gameState, player);

  int max = Math.max(maxRow, maxCol);
  max = Math.max(max, maxDiag);
  return max;
}

private static int calcDepth(final GameState gameState){
  int depth = 0;
   for (int index = 0; index < gameState.CELL_COUNT; index++) {
     if (gameState.at(index) == 0)
     depth++;
   }
   return depth;
}
public static int getotherP (int player){
  int otherP = (player == 1? 2 :1);
  return otherP;
}

public static int eval(final GameState gameState, int depth){
  int score = evalPart(gameState, depth);
  int myMax = maxInaRow(gameState, Constants.CELL_X);
  int otherPMax = maxInaRow(gameState, Constants.CELL_O);

  if(otherPMax == 4) score -= -100;
  if(myMax == 4) score += 100;

  score += depth;
  return score;
}

public static GameState alphabetasearch(GameState gameState){
  Vector<GameState> nextStates = new Vector<GameState>();
  gameState.findPossibleMoves(nextStates);

  int depth = calcDepth(gameState);
  totalDepth = depth;
  int maxdepth = (depth <= lowerThreshold? 0 : (int) ((double)depth * 0.65));

  int alpha = Integer.MIN_VALUE;
  int beta = Integer.MAX_VALUE;

  int player = gameState.getNextPlayer();

  int bestVal = Integer.MIN_VALUE;
  int current = Integer.MAX_VALUE;

  int bestGameState = -1;
  for (int i = 0; i < nextStates.size(); i++) {
    current = alphabetasearch(nextStates.elementAt(i), depth-1, alpha, beta, getotherP(player), maxdepth);

    if (current > bestVal) {
      bestVal = current;
      bestGameState = i;
    }
  }
    return nextStates.elementAt(bestGameState);
}

  public static int alphabetasearch(GameState gameState, int depth, int alpha, int beta, int player, int maxdepth){

    Vector<GameState> nextStates = new Vector<GameState>();
    gameState.findPossibleMoves(nextStates);

    int v;

    if (depth == maxdepth || nextStates.size() == 0) {
      v = eval(gameState, depth);
    }else if(player == Constants.CELL_X){
      v = Integer.MIN_VALUE;
      for (int i = 0; i < nextStates.size(); i++) {
        v = Math.max(v, alphabetasearch(nextStates.elementAt(i), depth-1, alpha, beta,Constants.CELL_O, (depth <= lowerThreshold? 0 : maxdepth)));
        alpha = Math.max(v, alpha);
        if(alpha >= beta){
          break;
        }
      }
    }else{
      v = Integer.MAX_VALUE;
      for (int i = 0; i < nextStates.size(); i++) {
        v = Math.min(v, alphabetasearch(nextStates.elementAt(i), depth-1, alpha, beta,Constants.CELL_X, (depth <= lowerThreshold? 0 : maxdepth)));
        beta = Math.min(v, beta);
        if (alpha >= beta) {
          break;
        }
      }
    }
    if(v == Integer.MAX_VALUE){
   		System.err.println("nextStates.size() = " + nextStates.size());
   		throw new RuntimeException("WWWWAT");
   	}

   	return v;
}
}
