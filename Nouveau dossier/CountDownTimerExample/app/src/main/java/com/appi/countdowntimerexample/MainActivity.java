package com.appi.countdowntimerexample;

import androidx.appcompat.app.AppCompatActivity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;
/*
 sharedPrefenrences Interface d’accès et de modification des données de préférence
 Un toast est une vue contenant un petit message rapide pour l'utilisateur
mENDTime l temps courant + le temps restant


 */

public class MainActivity extends AppCompatActivity {

    private EditText mEditTextInput ;
    private TextView mTextViewCountDown;
    private Button  mButtonSet ;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private CountDownTimer mCountDownTimer;
    ImageView imageView ;
    Animation rotate ;
    private boolean mTimerRunning;

    private  long mStartTimeInMillis ;
    private long mTimeLeftInMillis ;
    private long mENDTime;
    /*
       mStartTimeInMillis le nombre en millis défini au début
       mTimeLeftInMillis Le nombre de millis à venir de l'appel jusqu'à ce que le compte à rebours soit terminé
     */


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextInput = findViewById(R.id.edit_text_input);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        rotate = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotation);

        imageView = findViewById(R.id.imageView);
        mButtonSet = findViewById(R.id.button_set);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);

        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String input = mEditTextInput.getText().toString();
                 if(input.length()== 0){
                     Toast.makeText(MainActivity.this, " Field can't b empty", Toast.LENGTH_SHORT).show();
                     return;
                     /*
                        si on met un champ vite et on clique sur set on aura le message
                      */
                 }

                 long millisInput = Long.parseLong(input)* 60000 ;
                 if(millisInput == 0 ){
                     Toast.makeText(MainActivity.this, "please enter a positive number", Toast.LENGTH_SHORT).show();
                     return;
                     /*
                      si on ecrit un nombe négatif
                      */
                 }
                 setTime(millisInput);
                 /*
                   le temps prendra alors la valeur saisie par l'utilisateur
                  */
                 mEditTextInput.setText("");
            }
        });
        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                if (mTimerRunning) {
                    pauseTimer();

                } else {
                    startTimer();


                }


            }




        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                resetTimer();

            }
        });


    }

     private void setTime(long milliseconds){
        mStartTimeInMillis = milliseconds ;
        resetTimer();
        closeKeyboard();
        /*
         pour ne pas afficher toujours le clavier ce qui est génant
         */
     }

    private void startTimer() {
        mENDTime= System.currentTimeMillis()+ mTimeLeftInMillis ;
         /*
          mENDTime l temps courant et le temps restant
          */

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
             /*
                compte à rebours jusqu'au nombre millis restant
                avec des notifications régulières sur l'interval
                Les appels à onTick(long)sont synchronisés avec cet interval  afin qu'un appel à
                onTick(long)ne se produise jamais avant la fin du rappel précédent
              */

            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();


            }

            public void onFinish() {
                mTimerRunning = false;
               updateWatchingInterface();
            }
        }.start();
        mTimerRunning = true;
        imageView.startAnimation(rotate);

        updateWatchingInterface();
    }
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        rotate.cancel();

        updateWatchingInterface();
    }


        private void resetTimer(){
            mTimeLeftInMillis = mStartTimeInMillis;
            /*
              on va réinitialiser le compte à reboursà  la saisie précédente en récupérant mStartMillis du début
             */
            updateCountDownText();
            updateWatchingInterface();

        }
        private void updateCountDownText(){
            int hours = (int) (mTimeLeftInMillis/1000)/3600;
            int minutes = (int) ((mTimeLeftInMillis / 1000) %3600)/ 60;
            int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
            String timeLeftFormatted ;
             if(hours>0){
                 timeLeftFormatted = String.format(Locale.getDefault(), " %d:%02d : %02d ", hours, minutes, seconds);
             } else {
                 timeLeftFormatted = String.format(Locale.getDefault(), "%02d : %02d " , minutes, seconds);

             }

                  /*
                    arranger l'affichage si jamais l'utilisateur saisi un grand inversement
                     un petit chiffre et convertir les milis restante  secodne en second , minutes et heures
                     passer de 65 minutes à a 1h et 5 minutes ...
                   */

            mTextViewCountDown.setText(timeLeftFormatted);

        }

        private void updateWatchingInterface(){
           if(mTimerRunning){
               mEditTextInput.setVisibility(View.INVISIBLE);
               mButtonSet.setVisibility(View.INVISIBLE);
               mButtonReset.setVisibility(View.INVISIBLE);
               mButtonStartPause.setText("Pause");

           }else {
               mEditTextInput.setVisibility(View.VISIBLE);
               mButtonSet.setVisibility(View.VISIBLE);
               mButtonStartPause.setText("Start");

               if (mTimeLeftInMillis < 1000){
                   mButtonStartPause.setVisibility(View.INVISIBLE);

               }
               else {
                   mButtonStartPause.setVisibility(View.VISIBLE);
               }

               if(mTimeLeftInMillis < mStartTimeInMillis){
                   mButtonReset.setVisibility(View.VISIBLE);
               }
               else{
                   mButtonReset.setVisibility(View.INVISIBLE);
               }
           }
        }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /*
          Bundle est conteneur de toutes les données on inclu dedans des fonctions pour stocker les données
         */
        /*

          onSaveInstanceState est appelée généralement avant / après onStop()

Dans       sauvegarde les valeurs importantes du lot sous la forme de paires clé-valeur.
         */
        super.onSaveInstanceState(outState);
        /*
          Sur l'instance du bundle outState, nous ajoutons les paires clé-valeur.
         */
        outState.putLong("millisLeft", mTimeLeftInMillis);
        outState.putBoolean("timerRunning", mTimerRunning);
        outState.putLong("endTime", mENDTime);
    }

    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null ){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
            /*
             inputMethodManager  arbitre l’interaction entre les applications et la méthode de saisie actuelle.
             ferme le clavier après le clic sur le bouton
             */

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
         /*
           gader le timer en fonction meme si on ferme l'application  c'est pourquoi on l'appelle dans  l'état de l'activité
           onStop
          */
        SharedPreferences.Editor editor = preferences.edit();

           /*
            Les modifications apportées aux préférences doivent passer par un Editor
            editor objet pour garantir que les valeurs de préférence restent dans un état cohérent et
            contrôlées lorsqu'elles sont validées pour le stockage.

            */
             /*
              edit() Créez un nouvel éditeur pour ces préférences, grâce auquel vous pourrez modifier les
              données dans les préférences
               et valider de manière atomique ces modifications dans l'objet SharedPreferences.
              */
        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime",mENDTime);

        editor.apply();
         /*
           selon les nouvelles  valeurs sur la mémoire on modifi l'état du compte  à rebour et de la rotation
          */
        if (mCountDownTimer!=null){
            mCountDownTimer.cancel();
            /*
             on arrête le compte à rebours si jamais il n'est pas arrivé a 0
             */
        }
        rotate.cancel();

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences("prefs",MODE_PRIVATE);
          /*
            (nom_du_fichier, mode) retourne une référence vers une SharedPreference qui correspond
             au nom de fichier nom_du_fichier, ouvert dans le mode mode.
           */
           /*
             MODE_PRIVATE, MODE_WORLD_READABLE,  MODE_WORLD_WRITABLE.
             Le fichier de préférences sera accessible à l’extérieur de l’application selon le mode utilisé
            */
        mStartTimeInMillis = preferences.getLong("startTimeInMillis",600000);
        mTimeLeftInMillis = preferences.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = preferences.getBoolean("timerRunning", false);
          /*
           initialisation des clés valeur au début
            exemple au début timerrunning est faux car il n'a pas encore débuté
           */

        updateCountDownText();
        updateWatchingInterface();

        if(mTimerRunning){
            mENDTime = preferences.getLong("endTime",0);
            mTimeLeftInMillis = mENDTime- System.currentTimeMillis();
            if(mTimeLeftInMillis<0){
                mTimeLeftInMillis = 0 ;
                mTimerRunning = false ;
                updateCountDownText();
                updateWatchingInterface();
            } else {
                startTimer();

            }
        }
    }
}
