/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demococaro;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;

/**
 *
 * @author T440s
 */
public class BanCo {
    
    private int soDong;
    private int soCot;
    
    public BanCo(){
        this.soCot = 0;
        this.soDong = 0;
    }
    
    public BanCo(int soDong, int soCot){
        this.soDong = soDong;
        this.soCot = soCot;
    }
    
    public void veBanCo(Graphics g){
        g.setColor(new Color(148,78,25));
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));
        for(int i = 0; i <= soCot; i++){
            g2.drawLine(i * OCo.CHIEURONG,0, i * OCo.CHIEURONG , soDong * OCo.CHIEUCAO);
        }
        for(int j = 0; j <= soDong; j++){
            g2.drawLine(0, j* OCo.CHIEUCAO, soCot * OCo.CHIEURONG, j * OCo.CHIEUCAO);
        }
    }
    
    public void veQuanCo(Graphics g, Point point, Color color, int luotchoi){
        g.setColor(color);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        if(luotchoi == 1){
            g2.drawLine(point.x + 6, point.y + 6, point.x + OCo.CHIEURONG -6 ,point.y + OCo.CHIEUCAO - 6);
            g2.drawLine(point.x + 6, point.y + OCo.CHIEUCAO - 6, point.x + OCo.CHIEURONG -6, point.y + 6 );
        }
        else{
            g2.drawOval(point.x + 4, point.y + 4, OCo.CHIEURONG - 8, OCo.CHIEUCAO - 8);
        }
    }

    public int getSoDong() {
        return soDong;
    }

    public void setSoDong(int soDong) {
        this.soDong = soDong;
    }

    public int getSoCot() {
        return soCot;
    }

    public void setSoCot(int soCot) {
        this.soCot = soCot;
    }
    
    
    
}
