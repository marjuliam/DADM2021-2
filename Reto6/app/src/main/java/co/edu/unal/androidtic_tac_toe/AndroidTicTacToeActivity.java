package co.edu.unal.androidtic_tac_toe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AndroidTicTacToeActivity extends AppCompatActivity {
    // Represents the internal state of the game
    private TicTacToeGame mGame;
    // Buttons making up the board
    private Button mBoardButtons[];
    // Various text displayed
    private TextView mInfoTextView,pcScore,human, empate;

    private boolean mGameOver;
    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;

    private int selected;
    private boolean humanStarts;
    private Integer humanSc=0,androidScore=0,empateScore=0;

    private BoardView mBoardView;

    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mComputerMediaPlayer;

    private int thisTurn=1;

    private SharedPreferences mPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGame = new TicTacToeGame();
        //reto6
        mPrefs = getSharedPreferences("ttt_prefs", MODE_PRIVATE);
        // Restore the scores
        humanSc = mPrefs.getInt("mHumanWins", 0);
        androidScore = mPrefs.getInt("mComputerWins", 0);
        empateScore = mPrefs.getInt("mTies", 0);

        mBoardView = (BoardView) findViewById(R.id.board);
        mBoardView.setGame(mGame);

        mBoardView.setOnTouchListener(mTouchListener);
        mInfoTextView = (TextView) findViewById(R.id.information);
        human = (TextView) findViewById(R.id.humanScore);
        pcScore = (TextView) findViewById(R.id.pcScore);
        empate = (TextView) findViewById(R.id.empate);
        human.setText(humanSc.toString());
        pcScore.setText(androidScore.toString());
        empate.setText(empateScore.toString());
        humanStarts=true;
        Toast.makeText(this,"",Toast.LENGTH_SHORT); //toast = ...
        selected = 2;
        //startNewGame();

        if (savedInstanceState == null) {
            startNewGame();
        }
        else {
            onRestoreInstanceState(savedInstanceState);
        }
        displayScores();
    }

    private void displayScores() {
        human.setText(humanSc.toString());
        pcScore.setText(androidScore.toString());
        empate.setText(empateScore.toString());
    }

    //reto6
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharArray("board", mGame.getBoardState());
        outState.putBoolean("mGameOver", mGameOver);
        outState.putInt("mHumanWins", Integer.valueOf(humanSc));
        outState.putInt("mComputerWins", Integer.valueOf(androidScore));
        outState.putInt("mTies", Integer.valueOf(empateScore));
        outState.putCharSequence("info", mInfoTextView.getText());
        outState.putBoolean("mGoFirst", humanStarts);
        outState.putInt("mThisTurn", Integer.valueOf(thisTurn));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mGame.setBoardState(savedInstanceState.getCharArray("board"));
        mGameOver = savedInstanceState.getBoolean("mGameOver");
        mInfoTextView.setText(savedInstanceState.getCharSequence("info"));
        humanSc = savedInstanceState.getInt("mHumanWins");
        androidScore = savedInstanceState.getInt("mComputerWins");
        empateScore = savedInstanceState.getInt("mTies");
        humanStarts = savedInstanceState.getBoolean("mGoFirst");
        thisTurn= savedInstanceState.getInt("mThisTurn");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Save the current scores
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("mHumanWins", humanSc);
        ed.putInt("mComputerWins", androidScore);
        ed.putInt("mTies", empateScore);
        ed.commit();
    }
    //reto6 end

    // Set up the game board.
    private void startNewGame() {
        mGame.clearBoard();
        mGameOver=false;

        mBoardView.invalidate();
        if(humanStarts) {
            mInfoTextView.setText("you go first.");
        }
        else {
            mInfoTextView.setText("android go first.");
            int move = mGame.getComputerMove();
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
        }
        humanStarts=!humanStarts;
    }

    // Handles clicks on the game board buttons
    private class ButtonClickListener implements View.OnClickListener {
        int location;
        public ButtonClickListener(int location) {
            this.location = location;
        }
        public void onClick(View view) {
            if (mBoardButtons[location].isEnabled() && mGameOver==false) {
                setMove(TicTacToeGame.HUMAN_PLAYER, location);
                // If no winner yet, let the computer make a move
                int winner = mGame.checkForWinner();
                if (winner == 0 && mGameOver == false) {
                    mInfoTextView.setText(R.string.turn_computer);
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }
                if (winner == 0) {
                    mInfoTextView.setText(R.string.turn_human);
                }else if (winner == 1) {
                    mInfoTextView.setText(R.string.result_tie);
                    mGameOver = true;
                }else if (winner == 2){
                    mInfoTextView.setText(R.string.result_human_wins);
                    mGameOver = true;
                }else{
                    mInfoTextView.setText(R.string.result_computer_wins);
                    mGameOver = true;
                }
            }
        }
    }

    private boolean setMove(char player, int location) {
        if (mGame.setMove(player, location)) {
            //set human media player.start()
            mBoardView.invalidate(); // Redraw the board
            return true;
        }

        return false;
    }

    public void reset(){
        humanSc=0;
        androidScore=0;
        empateScore=0;
        displayScores();
        startNewGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
        /*super.onCreateOptionsMenu(menu);
        menu.add("New Game");
        return true;*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
            case R.id.reset:
                reset();
                return true;
        }
        return false;
        /*startNewGame();
        return true;*/
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id) {
            case DIALOG_DIFFICULTY_ID:
                builder.setTitle(R.string.difficulty_choose);
                final CharSequence[] levels = {
                    getResources().getString(R.string.difficulty_easy),
                    getResources().getString(R.string.difficulty_harder),
                    getResources().getString(R.string.difficulty_expert)};
                // TODO: Set selected, an integer (0 to n-1), for the Difficulty dialog.
                // selected is the radio button that should be selected.
                builder.setSingleChoiceItems(levels, selected, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        dialog.dismiss(); // Close dialog
                        // TODO: Set the diff level of mGame based on which item was selected.
                        switch (item){
                            case 0:
                                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
                                break;
                            case 1:
                                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
                                break;
                            case 2:
                                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
                                break;
                        }
                        // Display the selected difficulty level
                        Toast.makeText(getApplicationContext(), levels[item],
                                Toast.LENGTH_SHORT).show();
                    }
                });
                dialog = builder.create();
                break;
            case DIALOG_QUIT_ID:
                // Create the quit confirmation dialog
                builder.setMessage(R.string.quit_question)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            AndroidTicTacToeActivity.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.no, null);
                dialog = builder.create();
                break;
        }
        return dialog;
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int col=(int) motionEvent.getX()/mBoardView.getBoardCellWidth();
            int row=(int) motionEvent.getY()/mBoardView.getBoardCellHeight();
            int pos=row*3+col;
            if(!mGameOver && setMove(TicTacToeGame.HUMAN_PLAYER,pos)){
                int winner = mGame.checkForWinner();
                if (winner == 0 ) {
                    mInfoTextView.setText("It's Android's turn.");
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    thisTurn=0;
                    mComputerMediaPlayer.start();
                    winner = mGame.checkForWinner();

                }
                boolean endgame = false;
                if (winner == 0) {
                    mInfoTextView.setText("It's your turn.");
                    thisTurn=1;
                    mHumanMediaPlayer.start();
                }
                else if (winner == 1)
                {
                    mInfoTextView.setText("It's a tie!");
                    mGameOver=true;
                    empateScore++;
                    empate.setText(empateScore.toString());
                    endgame =true;
                }

                else if (winner == 2) {
                    mInfoTextView.setText("You won!");
                    mGameOver = true;
                    humanSc++;
                    human.setText(humanSc.toString());
                    endgame =true;
                }
                else {
                    mInfoTextView.setText("Android won!");
                    mGameOver = true;
                    androidScore++;
                    pcScore.setText(androidScore.toString());
                    endgame =true;
                }
            }
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.zelda_tiruru);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.zelda_tiruru);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
    }

}
