package test;

import java.net.*;
import java.util.*;


import javafx.animation.*;
import javafx.animation.Animation.Status;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.util.*;

public class ImgInfoController 
implements Initializable{
    //これらの値は自動的にセットされる
    public AnchorPane infobox;
    public VBox contents_info;
    public Button sbutton;

    private boolean isOpen=true;

    //オーバーしたのを隠すためのクリップ
    private Rectangle clip;

    //サイズを変えるために、間に噛ませたプロパティ
    private DoubleProperty infoboxWidth=new SimpleDoubleProperty(1){
        public void set(double d) {
            super.set(d);
            //dによってmaxWidthを変化させる
            if(d==1d){
                infobox.setMaxWidth(-1);
                return;
            }
            double contents_info_width = contents_info.getWidth();
            double boxwidth = contents_info_width*d;
            infobox.setMaxWidth(boxwidth);
        }
    };

    private SequentialTransition openAnimation,closeAnimation;

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        clip = new Rectangle();
        infobox.setClip(clip);
        //clipの大きさをinfoboxの大きさに常に合わせる
        clip.widthProperty().bind(
                infobox.widthProperty());
        clip.heightProperty().bind(
                infobox.heightProperty());
        infobox.setMinWidth(0);
        sbutton.setRotate(-90);

        //アニメーション設定

        //スライドインさせるアニメーション
        Timeline slidein = new Timeline(
                new KeyFrame(new Duration(0), new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent e) {
                        if(closeAnimation.getStatus()==Status.RUNNING)
                            closeAnimation.stop();//ついでなので、クローズアニメーションをストップさせてみた
                    }

                }),
                new KeyFrame(new Duration(300),new KeyValue(infoboxWidth,1))
                );
        //フェードインさせるアニメーション
        //Timelineに追加してもいい気がしたけど、
        //練習のためにFadeTransitionを利用
        FadeTransition fadein = new FadeTransition(new Duration(300),infobox);
        fadein.setFromValue(0);
        fadein.setToValue(1);
        fadein.setDelay(new Duration(100));//ちょっと開始を遅らせる。

        //スライドとフェードを結合
        ParallelTransition open = new ParallelTransition(slidein,fadein);

        //スライドインが終わった後にボタンの向きを回転させる
        RotateTransition openrotate = new RotateTransition(new Duration(300),sbutton);
        openrotate.setToAngle(-90);
        openrotate.setFromAngle(90);
        openrotate.setDelay(new Duration(100));


        //openと回転を順次実行するアニメーション
        openAnimation = new SequentialTransition(open,openrotate);

        //スライドアウト
        Timeline slideout = new Timeline(
                new KeyFrame(new Duration(0), new EventHandler<ActionEvent>(){

                    @Override
                    public void handle(ActionEvent e) {
                        if(openAnimation.getStatus()==Status.RUNNING)
                            openAnimation.stop();
                    }

                }),
                new KeyFrame(new Duration(300),new KeyValue(infoboxWidth, 0))
                );
        //フェードアウト
        FadeTransition fadeout= new FadeTransition(new Duration(200),infobox);
        fadeout.setFromValue(1);
        fadeout.setToValue(0);

        //結合
        ParallelTransition close = new ParallelTransition(slideout,fadeout);

        //回転
        RotateTransition closerotate = new RotateTransition(new Duration(300),sbutton);
        closerotate.setToAngle(90);
        closerotate.setFromAngle(-90);
        closerotate.setDelay(new Duration(100));
        //結合
        closeAnimation = new SequentialTransition(close,closerotate);

    }

    public void slideAction(ActionEvent event){
        if(isOpen)closeAnimation.play();
        else openAnimation.play();
        isOpen = !isOpen;
    }
}
