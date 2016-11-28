/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demococaro;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author T440s
 */
public class CaroChess {

    public enum KETTHUC {
        HoaCo,
        Player1,
        Player2,
        Com
    }

    private OCo[][] mangOCo;
    private BanCo banco;
    private ArrayList<OCo> cacNuocDaDi;
    private int luotdi;
    private boolean sansang;
    private KETTHUC ketthuc;
    private int cheDoChoi;

    public int getLuotdi() {
        return luotdi;
    }

    public int getCheDoChoi() {
        return cheDoChoi;
    }

    public boolean isSansang() {
        return sansang;
    }

    public CaroChess() {
        banco = new BanCo(20, 20);
        mangOCo = new OCo[banco.getSoDong()][banco.getSoCot()];
        cacNuocDaDi = new ArrayList<OCo>();
        luotdi = 1;
    }

    public void veBanCo(Graphics g) {

        banco.veBanCo(g);

    }

    public void khoiTaoMangOCo() {
        for (int i = 0; i < banco.getSoDong(); i++) {
            for (int j = 0; j < banco.getSoCot(); j++) {
                mangOCo[i][j] = new OCo(i, j, new Point(j * OCo.CHIEUCAO, i * OCo.CHIEURONG), 0);
            }
        }
    }
    private int beforeDong = 0;
    private int beforeCot = 0;

    public boolean danhCo(int MouseX, int MouseY, Graphics g) {
        if (MouseX % OCo.CHIEURONG == 0 || MouseY % OCo.CHIEUCAO == 0) {
            return false;
        }
        int cot = MouseX / OCo.CHIEURONG;
        int dong = MouseY / OCo.CHIEUCAO;

        if (mangOCo[dong][cot].getSohuu() != 0) {
            return false;
        }

        switch (luotdi) {
            case 1:
                if (ktraNuocDoi(dong, cot, 1)) {
                    System.out.println("nuocdoi");
                }
                mangOCo[dong][cot].setSohuu(1);
                banco.veQuanCo(g, mangOCo[dong][cot].getVitri(), Color.RED, 1);
                luotdi = 2;
                break;
            case 2:
                mangOCo[dong][cot].setSohuu(2);
                banco.veQuanCo(g, mangOCo[dong][cot].getVitri(), Color.BLACK, 2);
                luotdi = 1;
                break;

            default:
                System.out.println("Có lỗi");

        }
        banco.veKhungOCo(g, mangOCo[beforeDong][beforeCot].getVitri(), new Color(243, 239, 228));
        this.veBanCo(g);
        banco.veKhungOCo(g, mangOCo[dong][cot].getVitri(), Color.BLACK);
        beforeDong = dong;
        beforeCot = cot;
        cacNuocDaDi.add(mangOCo[dong][cot]);
        return true;
    }

    public void veLaiQuanCo(Graphics g) {

        for (int i = 0; i < cacNuocDaDi.size(); i++) {
            if (cacNuocDaDi.get(i).getSohuu() == 1) {
                banco.veQuanCo(g, cacNuocDaDi.get(i).getVitri(), Color.RED, 1);
            } else if (cacNuocDaDi.get(i).getSohuu() == 2) {
                banco.veQuanCo(g, cacNuocDaDi.get(i).getVitri(), Color.BLACK, 2);
            }

        }
    }

    public void startPvsP(Graphics g) {
        sansang = true;
        cacNuocDaDi = new ArrayList<OCo>();
        luotdi = 1;
        cheDoChoi = 1;
        khoiTaoMangOCo();
        veBanCo(g);
    }

    public void startPlayervsCom(Graphics g) {
        sansang = true;
        cacNuocDaDi = new ArrayList<OCo>();
        luotdi = 1;
        cheDoChoi = 2;
        khoiTaoMangOCo();
        veBanCo(g);
        danhCo(OCo.CHIEURONG * 9 + 1, OCo.CHIEUCAO * 9 + 1, g);
    }

    public void khoiDongCom(Graphics g) {
        if (luotdi == 1) {
            OCo oco = TimKiemNuocDi();
            danhCo(oco.getVitri().x + 1, oco.getVitri().y + 1, g);
        }
    }

    public void ketThucTroChoi() {
        switch (ketthuc) {
            case HoaCo:
                JOptionPane.showMessageDialog(null, "Hòa cờ");
                break;
            case Player1:
                JOptionPane.showMessageDialog(null, "Người chơi 1 thắng");
                break;
            case Player2:
                JOptionPane.showMessageDialog(null, "Người chơi 2 thắng");
                break;
            case Com:
                JOptionPane.showMessageDialog(null, "Máy thắng");
                break;

        }
        sansang = false;
    }

    public boolean ktraChienThang() {
        if (cacNuocDaDi.size() == banco.getSoDong() * banco.getSoCot()) {
            ketthuc = KETTHUC.HoaCo;
            return true;
        }
        for (int i = 0; i < cacNuocDaDi.size(); i++) {
            OCo oco = cacNuocDaDi.get(i);
            if (duyetDoc(oco.getDong(), oco.getCot(), oco.getSohuu())
                    || duyetNgang(oco.getDong(), oco.getCot(), oco.getSohuu())
                    || duyetCheoXuoi(oco.getDong(), oco.getCot(), oco.getSohuu())
                    || duyetCheoNguoc(oco.getDong(), oco.getCot(), oco.getSohuu())) {
                ketthuc = oco.getSohuu() == 1 ? KETTHUC.Player1 : KETTHUC.Player2;
                return true;
            }
        }
        return false;
    }

    private boolean duyetDoc(int currDong, int currCot, int currSoHuu) {
        if (currDong > banco.getSoDong() - 5) {
            return false;
        }
        int dem;
        for (dem = 1; dem < 5; dem++) {
            if (mangOCo[currDong + dem][currCot].getSohuu() != currSoHuu) {
                return false;
            }
        }
        if (currDong == 0 || currDong + dem == banco.getSoDong()) {
            return true;
        }
        if (mangOCo[currDong - 1][currCot].getSohuu() == 0 || mangOCo[currDong + dem][currCot].getSohuu() == 0) {
            return true;
        }
        return false;
    }

    private boolean duyetNgang(int currDong, int currCot, int currSoHuu) {
        if (currCot > banco.getSoCot() - 5) {
            return false;
        }
        int dem;
        for (dem = 1; dem < 5; dem++) {
            if (mangOCo[currDong][currCot + dem].getSohuu() != currSoHuu) {
                return false;
            }
        }
        if (currCot == 0 || currCot + dem == banco.getSoCot()) {
            return true;
        }
        if (mangOCo[currDong][currCot - 1].getSohuu() == 0 || mangOCo[currDong][currCot + dem].getSohuu() == 0) {
            return true;
        }
        return false;
    }

    private boolean duyetCheoXuoi(int currDong, int currCot, int currSoHuu) {
        if (currDong > banco.getSoDong() - 5 || currCot > banco.getSoCot() - 5) {
            return false;
        }
        int dem;
        for (dem = 1; dem < 5; dem++) {
            if (mangOCo[currDong + dem][currCot + dem].getSohuu() != currSoHuu) {
                return false;
            }
        }
        if (currCot == 0 || currCot + dem == banco.getSoCot() || currDong == 0 || currDong + dem == banco.getSoDong()) {
            return true;
        }
        if (mangOCo[currDong - 1][currCot - 1].getSohuu() == 0 || mangOCo[currDong + dem][currCot + dem].getSohuu() == 0) {
            return true;
        }
        return false;
    }

    private boolean duyetCheoNguoc(int currDong, int currCot, int currSoHuu) {
        if (currDong < 4 || currCot > banco.getSoCot() - 5) {
            return false;
        }
        int dem;
        for (dem = 1; dem < 5; dem++) {
            if (mangOCo[currDong - dem][currCot + dem].getSohuu() != currSoHuu) {
                return false;
            }
        }
        if (currDong == 4 || currDong == banco.getSoDong() - 1 || currCot == 0 || currCot == banco.getSoCot()) {
            return true;
        }
        if (mangOCo[currDong + 1][currCot - 1].getSohuu() == 0 || mangOCo[currDong - dem][currCot + dem].getSohuu() == 0) {
            return true;
        }
        return false;
    }

    //kiểm tra nước đôi
    private boolean ktraNuocDoi(int dongHienTai, int cotHienTai, int luotdi) {
        int diemNuocDoi_T = 0, diemNuocDoi_TB = 0, diemNuocDoi_B = 0, diemNuocDoi_DB = 0, diemNuocDoi_D = 0, diemNuocDoi_DN = 0, diemNuocDoi_N = 0, diemNuocDoi_TN = 0, diemNuocDoi_T2 = 0, diemNuocDoi_TB2 = 0, diemNuocDoi_B2 = 0, diemNuocDoi_DB2 = 0, diemNuocDoi_D2 = 0, diemNuocDoi_DN2 = 0, diemNuocDoi_N2 = 0, diemNuocDoi_TN2 = 0;

        // xét hướng bắc
        for (int dem = 1; dem < 5; dem++) {
            if (dongHienTai - dem >= 0 && mangOCo[dongHienTai - dem][cotHienTai].getSohuu() == luotdi && mangOCo[dongHienTai - 1][cotHienTai].getSohuu() == luotdi) {
                diemNuocDoi_B++;
            } else if (dongHienTai - dem < 0 || mangOCo[dongHienTai - dem][cotHienTai].getSohuu() == thayDoiLuotDi(luotdi)) {
                diemNuocDoi_B--;
                break;
            } else {
                if (dongHienTai - dem - 1 >= 0 && mangOCo[dongHienTai - dem - 1][cotHienTai].getSohuu() == thayDoiLuotDi(luotdi)) {
                    diemNuocDoi_B--;
                } else if (diemNuocDoi_B > 0 && dongHienTai - dem - 1 >= 0 && mangOCo[dongHienTai - dem - 1][cotHienTai].getSohuu() == luotdi
                        && ((dongHienTai - dem - 2 >= 0 && mangOCo[dongHienTai - dem - 2][cotHienTai].getSohuu() == luotdi)
                        || (dongHienTai - dem - 2 >= 0 && mangOCo[dongHienTai - dem - 2][cotHienTai].getSohuu() == 0 
                        && dongHienTai - dem - 3 >= 0 && mangOCo[dongHienTai - dem - 3][cotHienTai].getSohuu() != thayDoiLuotDi(luotdi)))) {
                    diemNuocDoi_B++;
                }
                break;
            }

        }
        if (dongHienTai - 1 >= 0 && mangOCo[dongHienTai - 1][cotHienTai].getSohuu() == 0) {
            for (int dem2 = 2; dem2 < 6; dem2++) {
                if (dongHienTai - dem2 >= 0 && mangOCo[dongHienTai - dem2][cotHienTai].getSohuu() == luotdi) {
                    diemNuocDoi_B2++;
                } else if (dongHienTai - dem2 >= 0 && mangOCo[dongHienTai - dem2][cotHienTai].getSohuu() == thayDoiLuotDi(luotdi)) {
                    diemNuocDoi_B2--;
                    break;
                } else {
                    if (dongHienTai - dem2 - 1 >= 0 && mangOCo[dongHienTai - dem2 - 1][cotHienTai].getSohuu() == thayDoiLuotDi(luotdi)) {
                        diemNuocDoi_B2--;
                    }
                    break;
                }
            }
        }
        //xét hướng Nam
        for (int dem = 1; dem < 5; dem++) {
            if (dongHienTai + dem < banco.getSoDong() && mangOCo[dongHienTai + dem][cotHienTai].getSohuu() == luotdi && mangOCo[dongHienTai + 1][cotHienTai].getSohuu() == luotdi) {
                diemNuocDoi_N++;
            } else if (dongHienTai + dem >= banco.getSoDong() || mangOCo[dongHienTai + dem][cotHienTai].getSohuu() == thayDoiLuotDi(luotdi)) {
                diemNuocDoi_N--;
                break;
            } else {
                if (dongHienTai + dem + 1 < 20 && mangOCo[dongHienTai + dem + 1][cotHienTai].getSohuu() == thayDoiLuotDi(luotdi)) {
                    diemNuocDoi_N--;
                } else if (diemNuocDoi_N > 0 && dongHienTai + dem + 1 < banco.getSoDong() && mangOCo[dongHienTai + dem + 1][cotHienTai].getSohuu() == luotdi
                        && ((dongHienTai + dem + 2 < banco.getSoDong() && mangOCo[dongHienTai + dem + 2][cotHienTai].getSohuu() == luotdi)
                        || (dongHienTai + dem + 2 < banco.getSoDong() && mangOCo[dongHienTai + dem + 2][cotHienTai].getSohuu() == 0 
                        && dongHienTai + dem + 3 < banco.getSoDong() && mangOCo[dongHienTai + dem + 3][cotHienTai].getSohuu() != thayDoiLuotDi(luotdi))) ){
                    diemNuocDoi_N++;
                }
                break;
            }

        }
        if (dongHienTai + 1 < banco.getSoDong() && mangOCo[dongHienTai + 1][cotHienTai].getSohuu() == 0) {
            for (int dem2 = 2; dem2 < 6 && dongHienTai + dem2 < banco.getSoDong(); dem2++) {
                if (dongHienTai + dem2 < banco.getSoDong() && mangOCo[dongHienTai + dem2][cotHienTai].getSohuu() == luotdi) {
                    diemNuocDoi_N2++;
                } else if (dongHienTai + dem2 > banco.getSoDong() || mangOCo[dongHienTai + dem2][cotHienTai].getSohuu() == thayDoiLuotDi(luotdi)) {
                    diemNuocDoi_N2--;
                    break;
                } else {
                    if (dongHienTai + dem2 + 1 < banco.getSoDong() && mangOCo[dongHienTai + dem2 + 1][cotHienTai].getSohuu() == thayDoiLuotDi(luotdi)) {
                        diemNuocDoi_N2--;
                    }
                    break;
                }
            }
        }
        //xét hướng Tây
        for (int dem = 1; dem < 5; dem++) {
            if (cotHienTai - dem >= 0 && mangOCo[dongHienTai][cotHienTai - dem].getSohuu() == luotdi && mangOCo[dongHienTai][cotHienTai - 1].getSohuu() == luotdi) {
                diemNuocDoi_T++;
            } else if (cotHienTai - dem < 0 || mangOCo[dongHienTai][cotHienTai - dem].getSohuu() == thayDoiLuotDi(luotdi)) {
                diemNuocDoi_T--;
                break;
            } else {
                if (cotHienTai - dem - 1 >= 0 && mangOCo[dongHienTai][cotHienTai - dem - 1].getSohuu() == thayDoiLuotDi(luotdi)) {
                    diemNuocDoi_T--;
                } else if (diemNuocDoi_T > 0 && cotHienTai - dem - 1 >= 0 && mangOCo[dongHienTai][cotHienTai - dem - 1].getSohuu() == luotdi
                        && ((cotHienTai - dem - 2 >= 0 && mangOCo[dongHienTai][cotHienTai - dem - 2].getSohuu() == luotdi)  
                        || (cotHienTai - dem - 2 >= 0 && mangOCo[dongHienTai][cotHienTai - dem - 2].getSohuu() == 0 
                        && cotHienTai - dem - 3 >= 0 && mangOCo[dongHienTai][cotHienTai - dem - 3].getSohuu() != thayDoiLuotDi(luotdi)))) {
                    diemNuocDoi_T++;
                }

                break;
            }

        }
        if (cotHienTai - 1 >= 0 && mangOCo[dongHienTai][cotHienTai - 1].getSohuu() == 0) {
            for (int dem2 = 2; dem2 < 6 && cotHienTai - dem2 > 0; dem2++) {
                if (mangOCo[dongHienTai][cotHienTai - dem2].getSohuu() == luotdi) {
                    diemNuocDoi_T2++;
                } else if (mangOCo[dongHienTai][cotHienTai - dem2].getSohuu() == thayDoiLuotDi(luotdi)) {
                    diemNuocDoi_T2--;
                    break;
                } else {
                    if (cotHienTai - dem2 - 1 >= 0 && mangOCo[dongHienTai][cotHienTai - dem2 - 1].getSohuu() == thayDoiLuotDi(luotdi)) {
                        diemNuocDoi_T2--;
                    }

                    break;
                }
            }
        }
        // xét hướng Đông
        for (int dem = 1; dem < 5; dem++) {
            if (cotHienTai + dem < banco.getSoCot() && mangOCo[dongHienTai][cotHienTai + dem].getSohuu() == luotdi && mangOCo[dongHienTai][cotHienTai + 1].getSohuu() == luotdi) {
                diemNuocDoi_D++;
            } else if (cotHienTai + dem >= banco.getSoCot() || mangOCo[dongHienTai][cotHienTai + dem].getSohuu() == thayDoiLuotDi(luotdi)) {
                diemNuocDoi_D--;
                break;
            } else {
                if (cotHienTai + dem + 1 < banco.getSoCot() && mangOCo[dongHienTai][cotHienTai + dem + 1].getSohuu() == thayDoiLuotDi(luotdi)) {
                    diemNuocDoi_D--;
                } else if (diemNuocDoi_D > 0 && cotHienTai + dem + 1 < banco.getSoCot() && mangOCo[dongHienTai][cotHienTai + dem + 1].getSohuu() == luotdi
                        && ((cotHienTai + dem + 2 < banco.getSoCot() && mangOCo[dongHienTai][cotHienTai + dem + 2].getSohuu() == luotdi) 
                        || (cotHienTai + dem + 2 < banco.getSoCot() && mangOCo[dongHienTai][cotHienTai + dem + 2].getSohuu() == 0 
                        && cotHienTai + dem + 3 < banco.getSoCot() && mangOCo[dongHienTai][cotHienTai + dem + 3].getSohuu() != thayDoiLuotDi(luotdi)))) {
                    diemNuocDoi_T++;
                }
                break;
            }

        }
        if (cotHienTai + 1 < banco.getSoCot() && mangOCo[dongHienTai][cotHienTai + 1].getSohuu() == 0) {
            for (int dem2 = 2; dem2 < 6 && cotHienTai + dem2 < banco.getSoCot(); dem2++) {
                if (mangOCo[dongHienTai][cotHienTai + dem2].getSohuu() == luotdi) {
                    diemNuocDoi_D2++;
                } else if (mangOCo[dongHienTai][cotHienTai + dem2].getSohuu() == thayDoiLuotDi(luotdi)) {
                    diemNuocDoi_D2--;
                    break;
                } else {
                    if (cotHienTai + dem2 + 1 < banco.getSoCot() && mangOCo[dongHienTai][cotHienTai + dem2 + 1].getSohuu() == thayDoiLuotDi(luotdi)) {
                        diemNuocDoi_D2--;
                    }
                    break;
                }
            }
        }
        //xét hướng Đông Nam
        for (int dem = 1; dem < 5; dem++) {
            if (dongHienTai + dem < banco.getSoDong() && cotHienTai + dem < banco.getSoCot() && mangOCo[dongHienTai + dem][cotHienTai + dem].getSohuu() == luotdi && mangOCo[dongHienTai + 1][cotHienTai + 1].getSohuu() == luotdi) {
                diemNuocDoi_DN++;
            } else if (dongHienTai + dem >= banco.getSoDong() || cotHienTai + dem >= banco.getSoCot() || mangOCo[dongHienTai + dem][cotHienTai + dem].getSohuu() == thayDoiLuotDi(luotdi)) {
                diemNuocDoi_DN--;
                break;
            } else {
                if (dongHienTai + dem + 1 < 20 && cotHienTai + dem + 1 < banco.getSoDong() && mangOCo[dongHienTai + dem + 1][cotHienTai + dem + 1].getSohuu() == thayDoiLuotDi(luotdi)) {
                    diemNuocDoi_DN--;
                } else if (diemNuocDoi_DN > 0 && dongHienTai + dem + 1 < banco.getSoDong() && cotHienTai + dem + 1 < banco.getSoDong() && mangOCo[dongHienTai + dem + 1][cotHienTai + dem + 1].getSohuu() == luotdi
                        && ((dongHienTai + dem + 2 < banco.getSoDong() && cotHienTai + dem + 2 < banco.getSoDong() && mangOCo[dongHienTai + dem + 2][cotHienTai + dem + 2].getSohuu() == luotdi ) 
                        || (dongHienTai + dem + 2 < banco.getSoDong() && cotHienTai + dem + 2 < banco.getSoDong() && mangOCo[dongHienTai + dem + 2][cotHienTai + dem + 2].getSohuu() == 0 
                        && dongHienTai + dem + 3 < banco.getSoDong() && cotHienTai + dem + 3 < banco.getSoDong() && mangOCo[dongHienTai + dem + 3][cotHienTai + dem + 3].getSohuu() != thayDoiLuotDi(luotdi)))) {
                    diemNuocDoi_T++;
                }
                break;
            }

        }
        if (dongHienTai + 1 < banco.getSoDong() && cotHienTai + 1 < banco.getSoCot() && mangOCo[dongHienTai + 1][cotHienTai + 1].getSohuu() == 0) {
            for (int dem2 = 2; dem2 < 6 && dongHienTai + dem2 < banco.getSoDong() && cotHienTai + dem2 < banco.getSoCot(); dem2++) {
                if (mangOCo[dongHienTai + dem2][cotHienTai + dem2].getSohuu() == luotdi) {
                    diemNuocDoi_DN2++;
                } else if (mangOCo[dongHienTai + dem2][cotHienTai + dem2].getSohuu() == thayDoiLuotDi(luotdi)) {
                    diemNuocDoi_DN2--;
                    break;
                } else {
                    if (dongHienTai + dem2 + 1 < banco.getSoDong() && cotHienTai + dem2 + 1 < banco.getSoDong() && mangOCo[dongHienTai + dem2 + 1][cotHienTai + dem2 + 1].getSohuu() == thayDoiLuotDi(luotdi)) {
                        diemNuocDoi_DN2--;
                    }
                    break;
                }
            }
        }
        // xét hướng tây bắc
        for (int dem = 1; dem < 5; dem++) {
            if (dongHienTai - dem >= 0 && cotHienTai - dem >= 0 && mangOCo[dongHienTai - dem][cotHienTai - dem].getSohuu() == luotdi && mangOCo[dongHienTai - 1][cotHienTai - 1].getSohuu() == luotdi) {
                diemNuocDoi_TB++;
            } else if (dongHienTai - dem < 0 || cotHienTai - dem < 0 || mangOCo[dongHienTai - dem][cotHienTai - dem].getSohuu() == thayDoiLuotDi(luotdi)) {
                diemNuocDoi_TB--;
                break;
            } else {
                if (dongHienTai - dem - 1 >= 0 && cotHienTai - dem - 1 >= 0 && diemNuocDoi_TB > 0 && mangOCo[dongHienTai - dem - 1][cotHienTai - dem - 1].getSohuu() == thayDoiLuotDi(luotdi)) {
                    diemNuocDoi_TB--;
                } else if (diemNuocDoi_TB > 0 && dongHienTai - dem - 1 >= 0 && cotHienTai - dem - 1 >= 0 && mangOCo[dongHienTai - dem - 1][cotHienTai - dem - 1].getSohuu() == luotdi
                        && ((dongHienTai - dem - 2 >= 0 && cotHienTai - dem - 2 >= 0 && mangOCo[dongHienTai - dem - 2][cotHienTai - dem - 2].getSohuu() == luotdi) 
                        || (dongHienTai - dem - 2 >= 0 && cotHienTai - dem - 2 >= 0 && mangOCo[dongHienTai - dem - 2][cotHienTai - dem - 2].getSohuu() == 0 
                        && dongHienTai - dem - 3 >= 0 && cotHienTai - dem - 3 >= 0 && mangOCo[dongHienTai - dem - 3][cotHienTai - dem - 3].getSohuu() != thayDoiLuotDi(luotdi)))) {
                    diemNuocDoi_TB++;
                }
                break;
            }

        }
        if (dongHienTai - 1 >= 0 && cotHienTai - 1 >= 0 && mangOCo[dongHienTai - 1][cotHienTai - 1].getSohuu() == 0) {
            for (int dem2 = 2; dem2 < 6 && dongHienTai - dem2 > 0 && cotHienTai - dem2 > 0; dem2++) {
                if (mangOCo[dongHienTai - dem2][cotHienTai - dem2].getSohuu() == luotdi) {
                    diemNuocDoi_TB2++;
                } else if (mangOCo[dongHienTai - dem2][cotHienTai - dem2].getSohuu() == thayDoiLuotDi(luotdi)) {
                    diemNuocDoi_TB2--;
                    break;
                } else {
                    if (dongHienTai - dem2 - 1 >= 0 && cotHienTai - dem2 - 1 >= 0 && mangOCo[dongHienTai - dem2 - 1][cotHienTai - dem2 - 1].getSohuu() == thayDoiLuotDi(luotdi)) {
                        diemNuocDoi_TB2--;
                    }
                    break;
                }
            }
        }
        //xét hướng Tây Nam
        for (int dem = 1; dem < 5; dem++) {
            if (dongHienTai + dem < banco.getSoDong() && cotHienTai - dem >= 0 && mangOCo[dongHienTai + dem][cotHienTai - dem].getSohuu() == luotdi && mangOCo[dongHienTai + 1][cotHienTai - 1].getSohuu() == luotdi) {
                diemNuocDoi_TN++;
            } else if (dongHienTai + dem >= banco.getSoDong() || cotHienTai - dem < 0 || mangOCo[dongHienTai + dem][cotHienTai - dem].getSohuu() == thayDoiLuotDi(luotdi)) {
                diemNuocDoi_TN--;
                break;
            } else {
                if (diemNuocDoi_TN > 0 && dongHienTai + dem + 1 < banco.getSoDong() && cotHienTai - dem - 1 >= 0 && mangOCo[dongHienTai + dem + 1][cotHienTai - dem - 1].getSohuu() == thayDoiLuotDi(luotdi)) {
                    diemNuocDoi_TN--;
                } else if (diemNuocDoi_TN > 0 && dongHienTai + dem + 1 < banco.getSoDong() && cotHienTai - dem - 1 >= 0 && mangOCo[dongHienTai + dem + 1][cotHienTai - dem - 1].getSohuu() == luotdi
                        && ((dongHienTai + dem + 2 < banco.getSoDong() && cotHienTai - dem - 2 >= 0 && mangOCo[dongHienTai + dem + 2][cotHienTai - dem - 2].getSohuu() == luotdi)
                        || (dongHienTai + dem + 2 < banco.getSoDong() && cotHienTai - dem - 2 >= 0 && mangOCo[dongHienTai + dem + 2][cotHienTai - dem - 2].getSohuu() == 0
                        && dongHienTai + dem + 3 < banco.getSoDong() && cotHienTai - dem - 3 >= 0 && mangOCo[dongHienTai + dem + 3][cotHienTai - dem - 3].getSohuu() != thayDoiLuotDi(luotdi)))) {
                    diemNuocDoi_TN++;
                }
                break;
            }

        }
        if (dongHienTai + 1 < banco.getSoDong() && cotHienTai - 1 >= 0 && mangOCo[dongHienTai + 1][cotHienTai - 1].getSohuu() == 0) {
            for (int dem2 = 2; dem2 < 6 && dongHienTai + dem2 < banco.getSoDong() && cotHienTai - dem2 > 0; dem2++) {
                if (mangOCo[dongHienTai + dem2][cotHienTai - dem2].getSohuu() == luotdi) {
                    diemNuocDoi_TN2++;
                } else if (mangOCo[dongHienTai + dem2][cotHienTai - dem2].getSohuu() == thayDoiLuotDi(luotdi)) {
                    diemNuocDoi_TN2--;
                    break;
                } else {
                    if (dongHienTai + dem2 + 1 < banco.getSoDong() && cotHienTai - dem2 - 1 >= 0 && mangOCo[dongHienTai + dem2 + 1][cotHienTai - dem2 - 1].getSohuu() == thayDoiLuotDi(luotdi)) {
                        diemNuocDoi_TN2--;
                    }
                    break;
                }
            }
        }
        //xét hướng đông bắc
        for (int dem = 1; dem < 5; dem++) {
            if (dongHienTai - dem >= 0 && cotHienTai + dem < banco.getSoCot() && mangOCo[dongHienTai - dem][cotHienTai + dem].getSohuu() == luotdi && mangOCo[dongHienTai - 1][cotHienTai + 1].getSohuu() == luotdi) {
                diemNuocDoi_DB++;
            } else if (dongHienTai - dem < 0 || cotHienTai + dem >= banco.getSoCot() || mangOCo[dongHienTai - dem][cotHienTai + dem].getSohuu() == thayDoiLuotDi(luotdi)) {
                diemNuocDoi_DB--;
                break;
            } else {
                if (diemNuocDoi_DB > 0 && dongHienTai - dem - 1 >= 0 && cotHienTai + dem + 1 < banco.getSoDong() && mangOCo[dongHienTai - dem - 1][cotHienTai + dem + 1].getSohuu() == thayDoiLuotDi(luotdi)) {
                    diemNuocDoi_DB--;
                } else if (diemNuocDoi_DB > 0 && dongHienTai - dem - 1 >= 0 && cotHienTai + dem + 1 < banco.getSoDong() && mangOCo[dongHienTai - dem - 1][cotHienTai + dem + 1].getSohuu() == luotdi
                        && ((dongHienTai - dem - 2 >= 0 && cotHienTai + dem + 2 < banco.getSoDong() && mangOCo[dongHienTai - dem - 2][cotHienTai + dem + 2].getSohuu() == luotdi)
                        ||(dongHienTai - dem - 2 >= 0 && cotHienTai + dem + 2 < banco.getSoDong() && mangOCo[dongHienTai - dem - 2][cotHienTai + dem + 2].getSohuu() == 0 
                        && dongHienTai - dem - 3 >= 0 && cotHienTai + dem + 3 < banco.getSoDong() && mangOCo[dongHienTai - dem - 3][cotHienTai + dem + 3].getSohuu() != thayDoiLuotDi(luotdi)))) {
                    diemNuocDoi_DB++;
                }
                break;
            }
        }

        if (dongHienTai - 1 >= 0 && cotHienTai + 1 < banco.getSoCot() && mangOCo[dongHienTai - 1][cotHienTai + 1].getSohuu() == 0) {
            for (int dem2 = 2; dem2 < 6 && dongHienTai - dem2 > 0 && cotHienTai + dem2 < banco.getSoCot(); dem2++) {
                if (mangOCo[dongHienTai - dem2][cotHienTai + dem2].getSohuu() == luotdi) {
                    diemNuocDoi_DB2++;
                } else if (mangOCo[dongHienTai - dem2][cotHienTai + dem2].getSohuu() == thayDoiLuotDi(luotdi)) {
                    diemNuocDoi_DB2--;
                    break;
                } else {
                    if (dongHienTai - dem2 - 1 >= 0 && cotHienTai + dem2 + 1 < banco.getSoDong() && mangOCo[dongHienTai - dem2 - 1][cotHienTai + dem2 + 1].getSohuu() == thayDoiLuotDi(luotdi)) {
                        diemNuocDoi_DB2--;
                    }
                    break;
                }
            }
        }

//        System.out.println("currDong : " + dongHienTai + " \ncurrCot :" + cotHienTai);
//        System.out.println("----------------------------------");
//        System.out.println("Tay: " + diemNuocDoi_T + " + " + diemNuocDoi_T2 + "\nTayBac: " + diemNuocDoi_TB + " + " + diemNuocDoi_TB2 + "\nBac: " + diemNuocDoi_B
//                + " + " + diemNuocDoi_B2 + "\nDongBac: " + diemNuocDoi_DB + " + " + diemNuocDoi_DB2 + "\nDong: " + diemNuocDoi_D + " + " + diemNuocDoi_D2
//                + "\nDongNam: " + diemNuocDoi_DN + " +" + diemNuocDoi_DN2 + "\nNam: " + diemNuocDoi_N + " + " + diemNuocDoi_N2 + "\nTayNam: " + diemNuocDoi_TN + " + " + diemNuocDoi_TN2
//                + "\n------------------------\n");
        int soNuocDoi = 0;
        if ((diemNuocDoi_T + diemNuocDoi_T2 >= 2 && diemNuocDoi_D >= 0 && diemNuocDoi_D2 >= 0)
                || (diemNuocDoi_D + diemNuocDoi_D2 >= 2 && diemNuocDoi_T >= 0 && diemNuocDoi_T2 >= 0)
                || (diemNuocDoi_T == 1 && diemNuocDoi_D == 1 && diemNuocDoi_T2 >= 0 && diemNuocDoi_D2 >= 0)
                || (diemNuocDoi_T == 1 && diemNuocDoi_T2 >= 0 && diemNuocDoi_D2 == 1)
                || (diemNuocDoi_T2 == 1 && diemNuocDoi_D == 1 && diemNuocDoi_D2 >= 0)) {
            soNuocDoi++;
        }
        if ((diemNuocDoi_TB + diemNuocDoi_TB2 >= 2 && diemNuocDoi_DN >= 0 && diemNuocDoi_DN2 >= 0)
                || (diemNuocDoi_DN + diemNuocDoi_DN2 >= 2 && diemNuocDoi_TB >= 0 && diemNuocDoi_TB2 >= 0)
                || (diemNuocDoi_TB == 1 && diemNuocDoi_DN == 1 && diemNuocDoi_TB2 >= 0 && diemNuocDoi_DN2 >= 0)
                || (diemNuocDoi_TB == 1 && diemNuocDoi_TB2 >= 0 && diemNuocDoi_DN2 >= 1)
                || (diemNuocDoi_TB2 >= 1 && diemNuocDoi_DN == 1 && diemNuocDoi_DN2 >= 0)) {
            soNuocDoi++;
        }
        if ((diemNuocDoi_B + diemNuocDoi_B2 >= 2 && diemNuocDoi_N >= 0 && diemNuocDoi_N2 >= 0)
                || (diemNuocDoi_N + diemNuocDoi_N2 >= 2 && diemNuocDoi_B >= 0 && diemNuocDoi_B2 >= 0)
                || (diemNuocDoi_B == 1 && diemNuocDoi_N == 1 && diemNuocDoi_B2 >= 0 && diemNuocDoi_N2 >= 0)
                || (diemNuocDoi_B == 1 && diemNuocDoi_B2 >= 0 && diemNuocDoi_N2 >= 1)
                || (diemNuocDoi_B2 >= 1 && diemNuocDoi_N == 1 && diemNuocDoi_N2 >= 0)) {
            soNuocDoi++;
        }
        if ((diemNuocDoi_DB + diemNuocDoi_DB2 >= 2 && diemNuocDoi_TN >= 0 && diemNuocDoi_TN2 >= 0)
                || (diemNuocDoi_TN + diemNuocDoi_TN2 >= 2 && diemNuocDoi_DB >= 0 && diemNuocDoi_DB2 >= 0)
                || (diemNuocDoi_TN == 1 && diemNuocDoi_DB == 1 && diemNuocDoi_TN2 >= 0 && diemNuocDoi_DB2 >= 0)
                || (diemNuocDoi_TN == 1 && diemNuocDoi_TN2 >= 0 && diemNuocDoi_DB2 >= 1)
                || (diemNuocDoi_TN2 >= 1 && diemNuocDoi_DB == 1 && diemNuocDoi_DB2 >= 0)) {
            soNuocDoi++;
        }
        return soNuocDoi > 1 ? true : false;

    }

    //kiểm tra 4 quân không bị chặn 2 đầu
    public boolean ktra4Quan() {
        if (cacNuocDaDi.size() == banco.getSoDong() * banco.getSoCot()) {
            ketthuc = KETTHUC.HoaCo;
            return true;
        }
        for (int i = 0; i < cacNuocDaDi.size(); i++) {
            OCo oco = cacNuocDaDi.get(i);
            if (duyetDoc4Quan(oco.getDong(), oco.getCot(), oco.getSohuu())
                    || duyetNgang4Quan(oco.getDong(), oco.getCot(), oco.getSohuu())
                    || duyetCheoXuoi4Quan(oco.getDong(), oco.getCot(), oco.getSohuu())
                    || duyetCheoNguoc4Quan(oco.getDong(), oco.getCot(), oco.getSohuu())) {
                return true;
            }
        }
        return false;
    }

    private boolean duyetDoc4Quan(int currDong, int currCot, int currSoHuu) {
        if (currDong > banco.getSoDong() - 4) {
            return false;
        }
        int dem;
        for (dem = 1; dem < 4; dem++) {
            if (mangOCo[currDong + dem][currCot].getSohuu() != currSoHuu) {
                return false;
            }
        }
        if (currDong == 0 || currDong + dem == banco.getSoDong()) {
            return false;
        }
        if (mangOCo[currDong - 1][currCot].getSohuu() == 0 && mangOCo[currDong + dem][currCot].getSohuu() == 0
                && (((currDong - 2 >= 0) && mangOCo[currDong - 2][currCot].getSohuu() != thayDoiLuotDi(currSoHuu) || currDong == 1)
                && (currDong + dem + 1 <= banco.getSoDong() && mangOCo[currDong + dem + 1][currCot].getSohuu() != thayDoiLuotDi(currSoHuu) || currDong == 18))) {
            return true;
        }
        return false;
    }

    private boolean duyetNgang4Quan(int currDong, int currCot, int currSoHuu) {
        if (currCot > banco.getSoCot() - 4) {
            return false;
        }
        int dem;
        for (dem = 1; dem < 4; dem++) {
            if (mangOCo[currDong][currCot + dem].getSohuu() != currSoHuu) {
                return false;
            }
        }
        if (currCot == 0 || currCot + dem == banco.getSoCot()) {
            return false;
        }
        if (mangOCo[currDong][currCot - 1].getSohuu() == 0 && mangOCo[currDong][currCot + dem].getSohuu() == 0
                && (((currCot - 2 >= 0) && mangOCo[currDong][currCot - 2].getSohuu() != thayDoiLuotDi(currSoHuu) || currCot == 1)
                && (currCot + dem + 1 <= banco.getSoCot() && mangOCo[currDong][currCot + dem + 1].getSohuu() != thayDoiLuotDi(currSoHuu) || currCot == 18))) {
            return true;
        }
        return false;
    }

    private boolean duyetCheoXuoi4Quan(int currDong, int currCot, int currSoHuu) {
        if (currDong > banco.getSoDong() - 4 || currCot > banco.getSoCot() - 4) {
            return false;
        }
        int dem;
        for (dem = 1; dem < 4; dem++) {
            if (mangOCo[currDong + dem][currCot + dem].getSohuu() != currSoHuu) {
                return false;
            }
        }
        if (currCot == 0 || currCot + dem == banco.getSoCot() || currDong == 0 || currDong + dem == banco.getSoDong()) {
            return false;
        }
        if (mangOCo[currDong - 1][currCot - 1].getSohuu() == 0 && mangOCo[currDong + dem][currCot + dem].getSohuu() == 0
                && ((currDong - 2 >= 0 && currCot - 2 >= 0 && mangOCo[currDong - 2][currCot - 2].getSohuu() != thayDoiLuotDi(currSoHuu) || currDong == 1 || currCot == 1)
                && (currDong + dem + 1 <= banco.getSoDong() && currCot + dem + 1 <= banco.getSoCot() && mangOCo[currDong + dem + 1][currCot + dem + 1].getSohuu() != thayDoiLuotDi(currSoHuu) || currDong == 18 || currCot == 18))) {
            return true;
        }
        return false;
    }

    private boolean duyetCheoNguoc4Quan(int currDong, int currCot, int currSoHuu) {
        if (currDong < 3 || currCot > banco.getSoCot() - 4) {
            return false;
        }
        int dem;
        for (dem = 1; dem < 4; dem++) {
            if (mangOCo[currDong - dem][currCot + dem].getSohuu() != currSoHuu) {
                return false;
            }
        }
        if (currDong == 3 || currDong == banco.getSoDong() - 1 || currCot == 0 || currCot == banco.getSoCot()) {
            return false;
        }
        if (mangOCo[currDong + 1][currCot - 1].getSohuu() == 0 && mangOCo[currDong - dem][currCot + dem].getSohuu() == 0
                && ((currDong + 2 <= banco.getSoDong() && currCot - 2 >= 0 && mangOCo[currDong + 2][currCot - 2].getSohuu() != thayDoiLuotDi(currSoHuu) || currDong == 18 || currCot == 1)
                && (currDong - dem - 1 >= 0 && currCot + dem + 1 <= banco.getSoCot() && mangOCo[currDong - dem - 1][currCot + dem + 1].getSohuu() != thayDoiLuotDi(currSoHuu) || currDong == 1 || currCot == 18))) {
            return true;
        }
        return false;
    }

    // AI cho máy
    private long[] MangDiemTanCong = {0, 2, 18, 162, 1458, 1458 * 9, 1458 * 18};
    private long[] MangDiemPhongThu = {0, 1, 9, 81, 729, 729 * 9, 729 * 18};

    private long DiemTC_Doc(int dongHienTai, int cotHienTai, int luotdi) {
        int soQuanDich = 0;
        int soQuanTa = 0;
        long diemTong = 0;
        for (int dem = 1; dem < 6 && dongHienTai + dem < banco.getSoDong(); dem++) {
            if (mangOCo[dongHienTai + dem][cotHienTai].getSohuu() == luotdi) {
                soQuanTa++;
            } else if (mangOCo[dongHienTai + dem][cotHienTai].getSohuu() == thayDoiLuotDi(luotdi)) {
                soQuanDich++;
                break;
            } else {
                break;
            }
        }
        for (int dem = 1; dem < 6 && dongHienTai - dem >= 0; dem++) {
            if (mangOCo[dongHienTai - dem][cotHienTai].getSohuu() == luotdi) {
                soQuanTa++;
            } else if (mangOCo[dongHienTai - dem][cotHienTai].getSohuu() == thayDoiLuotDi(luotdi)) {
                soQuanDich++;
                break;
            } else {
                break;
            }

        }
        if (soQuanDich == 2) {
            return 0;
        }
        diemTong -= MangDiemPhongThu[soQuanDich];
        diemTong += MangDiemTanCong[soQuanTa];
        return diemTong;
    }

    private long DiemTC_Ngang(int dongHienTai, int cotHienTai, int luotdi) {
        int soQuanDich = 0;
        int soQuanTa = 0;
        long diemTong = 0;

        for (int dem = 1; dem < 6 && cotHienTai + dem < banco.getSoCot(); dem++) {
            if (mangOCo[dongHienTai][cotHienTai + dem].getSohuu() == luotdi) {
                soQuanTa++;
            } else if (mangOCo[dongHienTai][cotHienTai + dem].getSohuu() == thayDoiLuotDi(luotdi)) {
                soQuanDich++;
                break;
            } else {
                break;
            }

        }
        for (int dem = 1; dem < 6 && cotHienTai - dem >= 0; dem++) {
            if (mangOCo[dongHienTai][cotHienTai - dem].getSohuu() == luotdi) {
                soQuanTa++;
            } else if (mangOCo[dongHienTai][cotHienTai - dem].getSohuu() == thayDoiLuotDi(luotdi)) {
                soQuanDich++;
                break;
            } else {
                break;
            }

        }
        if (soQuanDich == 2) {
            return 0;
        }
        diemTong -= MangDiemPhongThu[soQuanDich];
        diemTong += MangDiemTanCong[soQuanTa];
        return diemTong;
    }

    private long DiemTC_CheoNguoc(int dongHienTai, int cotHienTai, int luotdi) {
        int soQuanDich = 0;
        int soQuanTa = 0;
        long diemTong = 0;
        for (int dem = 1; dem < 6 && cotHienTai + dem < banco.getSoCot() && dongHienTai + dem < banco.getSoDong(); dem++) {
            if (mangOCo[dongHienTai + dem][cotHienTai + dem].getSohuu() == luotdi) {
                soQuanTa++;
            } else if (mangOCo[dongHienTai + dem][cotHienTai + dem].getSohuu() == thayDoiLuotDi(luotdi)) {
                soQuanDich++;
                break;
            } else {
                break;
            }

        }
        for (int dem = 1; dem < 6 && cotHienTai - dem >= 0 && dongHienTai - dem >= 0; dem++) {
            if (mangOCo[dongHienTai - dem][cotHienTai - dem].getSohuu() == luotdi) {
                soQuanTa++;
            } else if (mangOCo[dongHienTai - dem][cotHienTai - dem].getSohuu() == thayDoiLuotDi(luotdi)) {
                soQuanDich++;
                break;
            } else // SoHuu = 0
            {
                break;
            }

        }
        if (soQuanDich == 2) {
            return 0;
        }
        diemTong -= MangDiemPhongThu[soQuanDich];
        diemTong += MangDiemTanCong[soQuanTa];
        return diemTong;
    }

    private long DiemTC_CheoXuoi(int dongHienTai, int cotHienTai, int luotdi) {
        int soQuanDich = 0;
        int soQuanTa = 0;
        long diemTong = 0;
        for (int dem = 1; dem < 6 && cotHienTai + dem < banco.getSoCot() && dongHienTai - dem > 0; dem++) {
            if (mangOCo[dongHienTai - dem][cotHienTai + dem].getSohuu() == luotdi) {
                soQuanTa++;
            } else if (mangOCo[dongHienTai - dem][cotHienTai + dem].getSohuu() == thayDoiLuotDi(luotdi)) {
                soQuanDich++;
                break;
            } else // SoHuu = 0
            {
                break;
            }

        }
        for (int dem = 1; dem < 6 && cotHienTai - dem >= 0 && dongHienTai + dem < banco.getSoDong(); dem++) {
            if (mangOCo[dongHienTai + dem][cotHienTai - dem].getSohuu() == luotdi) {
                soQuanTa++;
            } else if (mangOCo[dongHienTai + dem][cotHienTai - dem].getSohuu() == thayDoiLuotDi(luotdi)) {
                soQuanDich++;
                break;
            } else // SoHuu = 0
            {
                break;
            }

        }
        if (soQuanDich == 2) {
            return 0;
        }
        diemTong -= MangDiemPhongThu[soQuanDich];
        diemTong += MangDiemTanCong[soQuanTa];
        return diemTong;
    }

    private long DiemPN_Doc(int dongHienTai, int cotHienTai, int luotdi) {
        int soQuanDich = 0;
        int soQuanTa = 0;
        long diemTong = 0;
        for (int dem = 1; dem < 6 && dongHienTai + dem < banco.getSoDong(); dem++) {
            if (mangOCo[dongHienTai + dem][cotHienTai].getSohuu() == luotdi) {
                soQuanTa++;
                break;
            } else if (mangOCo[dongHienTai + dem][cotHienTai].getSohuu() == thayDoiLuotDi(luotdi)) {
                soQuanDich++;
            } else {
                break;
            }

        }
        for (int dem = 1; dem < 6 && dongHienTai - dem >= 0; dem++) {
            if (mangOCo[dongHienTai - dem][cotHienTai].getSohuu() == luotdi) {
                soQuanTa++;
                break;
            } else if (mangOCo[dongHienTai - dem][cotHienTai].getSohuu() == thayDoiLuotDi(luotdi)) {
                soQuanDich++;
            } else {
                break;
            }

        }
        if (soQuanTa == 2) {
            return 0;
        }
        diemTong -= MangDiemTanCong[soQuanTa];
        diemTong += MangDiemPhongThu[soQuanDich];
        return diemTong;
    }

    private long DiemPN_Ngang(int dongHienTai, int cotHienTai, int luotdi) {
        int soQuanDich = 0;
        int soQuanTa = 0;
        long diemTong = 0;
        for (int dem = 1; dem < 6 && cotHienTai + dem < banco.getSoCot(); dem++) {
            if (mangOCo[dongHienTai][cotHienTai + dem].getSohuu() == luotdi) {
                soQuanTa++;
                break;
            } else if (mangOCo[dongHienTai][cotHienTai + dem].getSohuu() == thayDoiLuotDi(luotdi)) {
                soQuanDich++;
            } else {
                break;
            }

        }
        for (int dem = 1; dem < 6 && cotHienTai - dem >= 0; dem++) {
            if (mangOCo[dongHienTai][cotHienTai - dem].getSohuu() == luotdi) {
                soQuanTa++;
                break;
            } else if (mangOCo[dongHienTai][cotHienTai - dem].getSohuu() == thayDoiLuotDi(luotdi)) {
                soQuanDich++;
            } else {
                break;
            }

        }
        if (soQuanTa == 2) {
            return 0;
        }
        diemTong -= MangDiemTanCong[soQuanTa];
        diemTong += MangDiemPhongThu[soQuanDich];
        if (soQuanDich >= 3 && soQuanTa == 0) {
            diemTong *= 100;
        }
        return diemTong;
    }

    private long DiemPN_CheoNguoc(int dongHienTai, int cotHienTai, int luotdi) {
        int soQuanDich = 0;
        int soQuanTa = 0;
        long diemTong = 0;
        for (int dem = 1; dem < 6 && cotHienTai + dem < banco.getSoCot() && dongHienTai - dem > 0; dem++) {
            if (mangOCo[dongHienTai - dem][cotHienTai + dem].getSohuu() == luotdi) {
                soQuanTa++;
                break;
            } else if (mangOCo[dongHienTai - dem][cotHienTai + dem].getSohuu() == thayDoiLuotDi(luotdi)) {
                soQuanDich++;
            } else {
                break;
            }

        }
        for (int dem = 1; dem < 6 && cotHienTai - dem >= 0 && dongHienTai + dem < banco.getSoDong(); dem++) {
            if (mangOCo[dongHienTai + dem][cotHienTai - dem].getSohuu() == luotdi) {
                soQuanTa++;
                break;
            } else if (mangOCo[dongHienTai + dem][cotHienTai - dem].getSohuu() == thayDoiLuotDi(luotdi)) {
                soQuanDich++;
            } else {
                break;
            }

        }
        if (soQuanTa == 2) {
            return 0;
        }
        diemTong -= MangDiemTanCong[soQuanTa];
        diemTong += MangDiemPhongThu[soQuanDich];
        if (soQuanDich >= 3 && soQuanTa == 0) {
            diemTong *= 100;
        }
        return diemTong;
    }

    private long DiemPN_CheoXuoi(int dongHienTai, int cotHienTai, int luotdi) {
        int soQuanDich = 0;
        int soQuanTa = 0;
        long diemTong = 0;
        for (int dem = 1; dem < 6 && cotHienTai + dem < banco.getSoCot() && dongHienTai + dem < banco.getSoDong(); dem++) {
            if (mangOCo[dongHienTai + dem][cotHienTai + dem].getSohuu() == luotdi) {
                soQuanTa++;
                break;
            } else if (mangOCo[dongHienTai + dem][cotHienTai + dem].getSohuu() == thayDoiLuotDi(luotdi)) {
                soQuanDich++;
            } else {
                break;
            }

        }
        for (int dem = 1; dem < 6 && cotHienTai - dem >= 0 && dongHienTai - dem >= 0; dem++) {
            if (mangOCo[dongHienTai - dem][cotHienTai - dem].getSohuu() == luotdi) {
                soQuanTa++;
                break;
            } else if (mangOCo[dongHienTai - dem][cotHienTai - dem].getSohuu() == thayDoiLuotDi(luotdi)) {
                soQuanDich++;
            } else {
                break;
            }

        }
        if (soQuanTa == 2) {
            return 0;
        }
        diemTong -= MangDiemTanCong[soQuanTa];
        diemTong += MangDiemPhongThu[soQuanDich];
        if (soQuanDich >= 3 && soQuanTa == 0) {
            diemTong *= 100;
        }
        return diemTong;
    }

    private long tinhDiemOCoCuoi(int i, int j, int luotdi) {
        return tongDiemTC(i, j, luotdi) + tongDiemPN(i, j, luotdi);
    }

    private long tongDiemTC(int i, int j, int luotdi) {
        return DiemTC_Doc(i, j, luotdi) + DiemTC_Ngang(i, j, luotdi) + DiemTC_CheoXuoi(i, j, luotdi) + DiemTC_CheoNguoc(i, j, luotdi);
    }

    private long tongDiemPN(int i, int j, int luotdi) {
        return DiemPN_Doc(i, j, luotdi) + DiemPN_Ngang(i, j, luotdi) + DiemPN_CheoXuoi(i, j, luotdi) + DiemPN_CheoNguoc(i, j, luotdi);
    }

    public int thayDoiLuotDi(int luotdi) {
        return luotdi == 1 ? 2 : 1;
    }

    private void traLaiTrangThai(int dong, int cot) {
        mangOCo[dong][cot].setSohuu(0);
        cacNuocDaDi.remove(cacNuocDaDi.size() - 1);
    }

    private boolean locOCo(int dong, int cot) {
        for (int i = 1; i < 4; i++) {
            if (dong + i < banco.getSoDong() && mangOCo[dong + i][cot].getSohuu() != 0) {
                return true;
            }
            if (dong - i >= 0 && mangOCo[dong - i][cot].getSohuu() != 0) {
                return true;
            }
            if (dong + i < banco.getSoDong() && cot + i < banco.getSoCot() && mangOCo[dong + i][cot + i].getSohuu() != 0) {
                return true;
            }
            if (dong - i >= 0 && cot - i >= 0 && mangOCo[dong - i][cot - i].getSohuu() != 0) {
                return true;
            }
            if (cot + i < banco.getSoCot() && mangOCo[dong][cot + i].getSohuu() != 0) {
                return true;
            }
            if (cot - i >= 0 && mangOCo[dong][cot - i].getSohuu() != 0) {
                return true;
            }
            if (dong + i < banco.getSoDong() && cot - i >= 0 && mangOCo[dong + i][cot - i].getSohuu() != 0) {
                return true;
            }
            if (dong - i >= 0 && cot + i < banco.getSoCot() && mangOCo[dong - i][cot + i].getSohuu() != 0) {
                return true;
            }
        }
        return false;
    }

    // MinMax
    public final int DOSAU = 2;
    private long alpha = Long.MAX_VALUE;
    private long beta = Long.MIN_VALUE;
    private long dem;

    private long MinMax(int dosau, int currDong, int currCot, int luotdi) {
        //System.out.println(dem++);
        dosau = dosau + 1;
        mangOCo[currDong][currCot].setSohuu(luotdi);
        cacNuocDaDi.add(mangOCo[currDong][currCot]);
        if (ktraChienThang()) {
            traLaiTrangThai(currDong, currCot);
            return dosau % 2 != 0 ? 1000000 : -1000000;
        }
        if (ktra4Quan()) {
            traLaiTrangThai(currDong, currCot);
            return dosau % 2 != 0 ? 500000 : -500000;
        }
        if (ktraNuocDoi(currDong, currCot, luotdi)) {
            traLaiTrangThai(currDong, currCot);
            return dosau % 2 != 0 ? 100000 : -100000;
        }
        if (dosau > DOSAU) {
            traLaiTrangThai(currDong, currCot);
            return tongDiemTC(currDong, currCot, luotdi);
        }
        long diemTam = 0;
        long diemMax = Long.MIN_VALUE;
        long diemMin = Long.MAX_VALUE;

        for (int i = 0; i < banco.getSoDong(); i++) {
            for (int j = 0; j < banco.getSoCot(); j++) {
                if (mangOCo[i][j].getSohuu() == 0 && locOCo(i, j)) {
                    diemTam = MinMax(dosau, i, j, thayDoiLuotDi(luotdi));
                    if (diemTam > diemMax) {
                        diemMax = diemTam;
                    }
                    if (diemTam < diemMin) {
                        diemMin = diemTam;
                    }
//                    if (diemMax > alpha && dosau % 2 == 0) {
//                        traLaiTrangThai(currDong, currCot);
//                        return diemMax;
//                    }
//                    if (diemMin < beta && dosau % 2 != 0) {
//                        traLaiTrangThai(currDong, currCot);
//                        return diemMin;
//                    }
                }
            }
        }
//        if (dosau % 2 == 0) {
//            alpha = diemMax;
//        }
//        if (dosau % 2 != 0) {
//            beta = diemMin;
//        }
        traLaiTrangThai(currDong, currCot);
        return dosau % 2 != 0 ? diemMin : diemMax;
    }

    private OCo TimKiemNuocDi() {

        ArrayList<OCo> mangOCoKQ_TC = new ArrayList<OCo>();
        ArrayList<OCo> mangOCoKQ_PN = new ArrayList<OCo>();
        OCo oCoTC_KQ = new OCo();
        OCo oCoPN_KQ = new OCo();
        long diemMaxTC = Long.MIN_VALUE;
        long diemMaxPN = Long.MIN_VALUE;
        for (int i = 0; i < banco.getSoDong(); i++) {
            for (int j = 0; j < banco.getSoCot(); j++) {
                if (mangOCo[i][j].getSohuu() == 0 && locOCo(i, j)) {
                    //tấn công
                    long diemTam = MinMax(0, i, j, 1);
                    if (diemMaxTC < diemTam) {
                        mangOCoKQ_TC.clear();
                        diemMaxTC = diemTam;
                        oCoTC_KQ = new OCo(mangOCo[i][j].getDong(), mangOCo[i][j].getCot(), mangOCo[i][j].getVitri(), mangOCo[i][j].getSohuu());
                    }
                    if (diemTam == diemMaxTC) {
                        mangOCoKQ_TC.add(new OCo(mangOCo[i][j].getDong(), mangOCo[i][j].getCot(), mangOCo[i][j].getVitri(), mangOCo[i][j].getSohuu()));
                    }
                }
            }
        }
        if (diemMaxTC < 100000) {
            long diemMax2 = Long.MIN_VALUE;
            for (int i = 0; i < banco.getSoDong(); i++) {
                for (int j = 0; j < banco.getSoCot(); j++) {
                    if (mangOCo[i][j].getSohuu() == 0 && locOCo(i, j)) {
                        long diemTam2 = MinMax(DOSAU, i, j, 1);
                        if (diemTam2 == 1000000) {
                            oCoTC_KQ = new OCo(mangOCo[i][j].getDong(), mangOCo[i][j].getCot(), mangOCo[i][j].getVitri(), mangOCo[i][j].getSohuu());
                            System.out.println("DiemTam: " + diemTam2);
                            return oCoTC_KQ;
                        }
                        if (diemMax2 < diemTam2) {
                            diemMax2 = diemTam2;
                            oCoTC_KQ = new OCo(mangOCo[i][j].getDong(), mangOCo[i][j].getCot(), mangOCo[i][j].getVitri(), mangOCo[i][j].getSohuu());
                        }
                    }
                }
            }
        }
        // phòng ngự
        alpha = Long.MAX_VALUE;
        beta = Long.MIN_VALUE;
        for (int i = 0; i < banco.getSoDong(); i++) {
            for (int j = 0; j < banco.getSoCot(); j++) {
                if (mangOCo[i][j].getSohuu() == 0 && locOCo(i, j)) {
                    long diemTam2 = MinMax(0, i, j, 2);
                    if (diemMaxPN < diemTam2) {
                        mangOCoKQ_PN.clear();
                        diemMaxPN = diemTam2;
                        oCoPN_KQ = new OCo(mangOCo[i][j].getDong(), mangOCo[i][j].getCot(), mangOCo[i][j].getVitri(), mangOCo[i][j].getSohuu());
                    }
                    if (diemTam2 == diemMaxPN) {
                        mangOCoKQ_PN.add(new OCo(mangOCo[i][j].getDong(), mangOCo[i][j].getCot(), mangOCo[i][j].getVitri(), mangOCo[i][j].getSohuu()));
                    }

                }
            }
        }

        System.out.println("-----------------\n\nDanh sách các nước có thể đi: ");
        for (int i = 0; i < mangOCoKQ_TC.size(); i++) {
            OCo ocoTC = new OCo();
            ocoTC = mangOCoKQ_TC.get(i);
            System.out.println("Vị trí tấn công : Dòng " + ocoTC.getDong() + " Cột " + ocoTC.getCot());
            if (tinhDiemOCoCuoi(ocoTC.getDong(), ocoTC.getCot(), 1) > tinhDiemOCoCuoi(oCoTC_KQ.getDong(), oCoTC_KQ.getCot(), 1)) {
                oCoTC_KQ = ocoTC;
            }
        }
        for (int i = 0; i < mangOCoKQ_PN.size(); i++) {
            OCo ocoPN = new OCo();
            ocoPN = mangOCoKQ_PN.get(i);
            System.out.println("Vị trí PN : Dòng " + ocoPN.getDong() + " Cột " + ocoPN.getCot());
            if (tinhDiemOCoCuoi(ocoPN.getDong(), ocoPN.getCot(), 2) > tinhDiemOCoCuoi(oCoPN_KQ.getDong(), oCoPN_KQ.getCot(), 2)) {
                oCoPN_KQ = ocoPN;
            }
        }
        System.out.println("\nVị trí TC tốt nhất : Dòng: " + oCoTC_KQ.getDong() + " Cột: " + oCoTC_KQ.getCot());
        System.out.println("Vị trí PN tốt nhất : Dòng: " + oCoPN_KQ.getDong() + "  Cột: " + oCoPN_KQ.getCot());
        System.out.println("Điểm Max TC và điểm Max PN : " + diemMaxTC + " <-> " + diemMaxPN);
        if (diemMaxPN < 100000 || diemMaxTC >= diemMaxPN) {
            System.out.println("--> tấn công");
            return oCoTC_KQ;
        }
        System.out.println("--> phòng ngự");
        return oCoPN_KQ;
    }
}
