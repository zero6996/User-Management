package cn.zero.util;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ImgGenerate {
    /**
     *
     * @param session
     * @return img对象
     */
    public BufferedImage getImg(HttpSession session){
        // 1. 创建一个对象，在内存中画图片(验证码图片对象)
        int width = 80;
        int height = 30;
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        // 2. 美化图片
        // 2.1 填充背景色
        Graphics g = image.getGraphics(); // 获取画笔对象
        g.setColor(Color.GRAY); // 设置画笔颜色为灰色
        g.fillRect(0,0,width,height); // 填充颜色

        // 产生随机验证码
        String checkCode = getCheckCode();
        // 将验证码放入到HttpSession中
        session.setAttribute("checkCode_session",checkCode);

        // 设置画笔颜色为黄色
        g.setColor(Color.yellow);
        // 设置字体大小
        g.setFont(new Font("黑体",Font.BOLD,24));
        // 向图片上写入验证码
        g.drawString(checkCode,15,25);
        // 返回生成好的图片对象
        return image;
//        ImageIO.write(image,"jpg",response.getOutputStream());
    }

    /**
     * 产生4位随机字符串
     * @return
     */
    private String getCheckCode(){
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int size = base.length();
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i=1;i<=4;i++){
            // 产生0~size-1的随机值
            int index = r.nextInt(size);
            // 在base字符串中获取下标为index的字符
            char c = base.charAt(index);
            // 将c放入到StringBuffer中
            sb.append(c);
        }
        return sb.toString();
    }

}
