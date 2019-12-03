 package com.appi.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
/*
bundle pour la communication entre composants
animation pour l’animation (rotation)

 */

 public class MainActivity extends AppCompatActivity {
    Button btnStart , btnFinish,btnPause;
    Chronometer chronometer ;
    Animation rotate ;
    ImageView imageView ;
    long pause ;
    private  boolean running;
    /*
     pause pour garder la trace du temps écoulé depuis le démarage du chrono
     running  pour faire des tests pour l’affichage des boutons

     */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
          on create pour créer et importer les boutons, chrono... via leurs identifiants
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btnStart);
        btnFinish = findViewById(R.id.btnFinish);
        btnPause = findViewById(R.id.btnPause);
        chronometer = findViewById(R.id.chronometer);
        imageView = findViewById(R.id.imageView);

        rotate = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotation);

        btnStart.setOnClickListener(new View.OnClickListener() {
            /*
             OnClickListener : pour l'écouter au clic de l'utilisateur

             */
            @Override
            public void onClick(View view) {
                /*
                  onClickpour exécuter des actions après que l'utilisateur ait cliqué sur le bouton
                 */
                if (!running) {
                     /*
                       on commence l’animation
                       on ajuste le chrono avant de le démarrer
                       on start le chronomètre

                      */
                    imageView.startAnimation(rotate);
                    chronometer.setBase(SystemClock.elapsedRealtime() - pause);
                      /*
                        premier appel pause = 0
                        SystemClock.elapsedRealtime() renvoie l'heure depuis le démarrage du système
                        et incluent la veille prolongée. Cette horloge est garantie monotone et
                        continue de fonctionner même lorsque le processeur est en mode d'économie
                        d'énergie. C'est donc la base recommandée pour la synchronisation par
                        intervalles à usage général.

                       */
                        /*
                          setBase   permet de définir le temps de base que le chronomètre prend
                           automatiquement comme référence

                           0-0 =0 à la base il prend comme réfénrence le temps 0

                         */
                    chronometer.start();
                    running = true;

                    btnStart.setVisibility(View.INVISIBLE);
                    btnFinish.setVisibility(View.VISIBLE);
                    btnPause.setVisibility(View.VISIBLE);
                     /*
                      met à jour la visibilité des boutons
                      */
                }
            }
        });



        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                chronometer.setBase(SystemClock.elapsedRealtime());
                pause = 0 ;
                rotate.cancel();
                imageView.setAnimation(rotate);
                running = false ;
                /*
                   affiche 0
                   la rotation s'arrête avec le chronoètre et il se réinitialise à 0
                 */


                btnStart.setVisibility(View.VISIBLE);
                btnFinish.setVisibility(View.INVISIBLE);
                btnPause.setVisibility(View.INVISIBLE);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (running) {
                    chronometer.stop();
                    pause = SystemClock.elapsedRealtime() - chronometer.getBase();
                    /*
                      getBase Renvoie l'heure de base telle que définie via setBase
                     */
                    running = false;


                    rotate.cancel();
                    imageView.setAnimation(rotate);
                    btnStart.setVisibility(View.VISIBLE);
                    btnFinish.setVisibility(View.VISIBLE);
                    btnPause.setVisibility(View.INVISIBLE);

                }
            }
        });
    }
}
