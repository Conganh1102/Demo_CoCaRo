/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demococaro;

import java.awt.Point;

/**
 *
 * @author T440s
 */
public class OCo {
    
    public static final int CHIEURONG = 25;
    public static final int CHIEUCAO = 25;
    
    private int dong;
    private int cot;
    private int sohuu;
    private Point vitri;
    
    public OCo(int dong, int cot, Point vitri, int sohuu){
        this.dong = dong;
        this.cot = cot;
        this.vitri = vitri;
        this.sohuu = sohuu;
    }
    public OCo(){
        
    }
    public int getSohuu() {
        return sohuu;
    }

    public void setSohuu(int sohuu) {
        this.sohuu = sohuu;
    }
    

    public int getDong() {
        return dong;
    }

    public void setDong(int dong) {
        this.dong = dong;
    }

    public int getCot() {
        return cot;
    }

    public void setCot(int cot) {
        this.cot = cot;
    }

    public Point getVitri() {
        return vitri;
    }

    public void setVitri(Point vitri) {
        this.vitri = vitri;
    }
    
    
    
}
