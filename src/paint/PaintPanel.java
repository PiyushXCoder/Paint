/*
 * The MIT License
 *
 * Copyright 2019 piyush.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package paint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author piyush
 */
public class PaintPanel extends Component implements MouseMotionListener, MouseListener
{
    public int width , height;
    public Color fcolor;
    public Color bcolor;
    public String shape;
    public Boolean isfilled;
    public int thick;
    public BufferedImage it;
    private ArrayList<Part> parts;
    private ArrayList<Part> undo;
    private Graphics2D gt;
    
    public PaintPanel()
    {
        width = 600;
        height = 300;
        parts = new ArrayList<>();
        undo = new ArrayList<>();
        fcolor = Color.BLACK;
        bcolor = Color.WHITE;
        shape = "pencil";
        isfilled = false;
        thick = 1;
        it = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        gt = it.createGraphics();
        gt.setColor(Color.white);
        gt.fillRect(0, 0, width, height);
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }
    
    public void draw(Graphics2D gr, Part part)
    {
        
        gr.setColor(part.fcolor);
        gr.setStroke(new java.awt.BasicStroke(part.thick));
        if(part.shape.equals("pencil"))
        {
            int x = part.cords.get(0).x;
            int y = part.cords.get(0).y;


            for(int i = 1; i < part.cords.size(); i++)
            {
                gr.drawLine(x, y, part.cords.get(i).x, part.cords.get(i).y);
                x = part.cords.get(i).x;
                y = part.cords.get(i).y;
            }
        }
        else if(part.shape.equals("line"))
        {
            if(part.cords.size() > 1) gr.drawLine(part.cords.get(0).x, part.cords.get(0).y, part.cords.get(1).x, part.cords.get(1).y);
        }
        else if(part.shape.equals("rect"))
        {
            if(part.cords.size() > 1)
            {
                int x1,y1,x2,y2;
                x1 = Math.min(part.cords.get(0).x, part.cords.get(1).x);
                y1 = Math.min(part.cords.get(0).y, part.cords.get(1).y);
                x2 = Math.max(part.cords.get(0).x, part.cords.get(1).x);
                y2 = Math.max(part.cords.get(0).y, part.cords.get(1).y);
                if(part.isfilled) {
                    gr.setColor(part.bcolor);
                    gr.fillRect(x1,y1,x2 - x1,y2 - y1);
                    gr.setColor(part.fcolor);
                    gr.drawRect(x1,y1,x2 - x1,y2 - y1);
                }
                else gr.drawRect(x1,y1,x2 - x1,y2 - y1);
            }
        }
        else if(part.shape.equals("oval"))
        {
            if(part.cords.size() > 1)
            {
                int x1,y1,x2,y2;
                x1 = Math.min(part.cords.get(0).x, part.cords.get(1).x);
                y1 = Math.min(part.cords.get(0).y, part.cords.get(1).y);
                x2 = Math.max(part.cords.get(0).x, part.cords.get(1).x);
                y2 = Math.max(part.cords.get(0).y, part.cords.get(1).y);
                if(part.isfilled) {
                    gr.setColor(part.bcolor);
                    gr.fillOval(x1,y1,x2 - x1,y2 - y1);
                    gr.setColor(part.fcolor);
                    gr.drawOval(x1,y1,x2 - x1,y2 - y1);
                }
                else gr.drawOval(x1,y1,x2 - x1,y2 - y1);
            }
        }
        else if(part.shape.equals("circle"))
        {
            if(part.cords.size() > 1)
            {
                int x1,y1,x2,y2,r;
                x1 = Math.min(part.cords.get(0).x, part.cords.get(1).x);
                y1 = Math.min(part.cords.get(0).y, part.cords.get(1).y);
                x2 = Math.max(part.cords.get(0).x, part.cords.get(1).x);
                y2 = Math.max(part.cords.get(0).y, part.cords.get(1).y);
                r = (int)Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
                if(part.isfilled) {
                    gr.setColor(part.bcolor);
                    gr.fillOval(part.cords.get(0).x - r,part.cords.get(0).y - r,2 * r,2 * r);
                    gr.setColor(part.fcolor);
                    gr.drawOval(part.cords.get(0).x - r,part.cords.get(0).y - r,2 * r,2 * r);
                }
                else gr.drawOval(part.cords.get(0).x - r,part.cords.get(0).y - r,2 * r,2 * r);
            }
        }
    }
    
    public void paintToBuffer()
    {
        if(parts.size() > 10)
        {
            Part part = parts.get(0);
            draw(gt, part);
            parts.remove(0);
        }
    }
    
    public void save()
    {
        BufferedImage buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buf.createGraphics();
        g.drawImage(it, 0,0,null);
        for(Part part : parts)
        {
            draw(g, part);
        }
        try {
            ImageIO.write(buf, "png", new File("image.png"));
        } catch (IOException ex) {
            Logger.getLogger(PaintPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void open(File file)
    {
        BufferedImage buff = null;
        try {
            buff = ImageIO.read(file);
        } catch (IOException ex) {
            Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null, ex);
        }
        width = buff.getWidth();
        height = buff.getHeight();
        this.setPreferredSize(new Dimension(width, height));
        it = buff;
        parts = new ArrayList<>();
        undo = new ArrayList<>();
        this.repaint();
    }
    
    public void newfile()
    {
        parts = new ArrayList<>();
        undo = new ArrayList<>();
        it = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        gt = it.createGraphics();
        gt.setColor(Color.white);
        gt.fillRect(0, 0, width, height);
        width = 600;
        height = 300;
        this.setSize(new Dimension(width, height));
        this.repaint();
    }
    
    public void undo()
    {
        if(parts.size() > 0)
        {
            undo.add(parts.get(parts.size()-1));
            parts.remove(parts.size()-1);
            this.repaint(); 
        }
    }
    
    public void redo()
    {
        if(undo.size() > 0)
        {
            parts.add(undo.get(undo.size()-1));
            undo.remove(undo.size()-1);
            this.repaint(); 
        }
    }
        
    @Override
    public void paint(Graphics g)
    {
        Graphics2D gr = (Graphics2D)g;
        gr.drawImage(it, 0,0,null);
        for(Part part : parts)
        {
            draw(gr, part);
        }
        
        paintToBuffer();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(shape.equals("pencil")) parts.get(parts.size() - 1).cords.add(new Cord(e.getX(), e.getY()));
        if(shape.equals("line") || shape.equals("rect") || shape.equals("oval") || shape.equals("circle"))
        {
            if(parts.get(parts.size() - 1).cords.size() > 1) parts.get(parts.size() - 1).cords.remove(1);
            parts.get(parts.size() - 1).cords.add(new Cord(e.getX(), e.getY()));
        }
        this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(undo.size() > 0) undo = new ArrayList<>();
        parts.add(new Part(shape, fcolor, bcolor,isfilled, thick));
        parts.get(parts.size() - 1).cords.add(new Cord(e.getX(), e.getY()));
        this.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(shape.equals("pencil")) parts.get(parts.size() - 1).cords.add(new Cord(e.getX(), e.getY()));
        if(shape.equals("line") || shape.equals("rect") || shape.equals("oval") || shape.equals("circle"))
        {
            if(parts.get(parts.size() - 1).cords.size() > 1) parts.get(parts.size() - 1).cords.remove(1);
            parts.get(parts.size() - 1).cords.add(new Cord(e.getX(), e.getY()));
        }
        this.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}

class Part
{
    String shape;
    ArrayList<Cord> cords;
    Color fcolor;
    Color bcolor;
    int thick;
    Boolean isfilled;
    
    public Part()
    {
        this.shape = "pencil";
        this.cords = new ArrayList<>();
        this.fcolor = Color.black;
        this.bcolor = Color.white;
        this.isfilled = false;
        this.thick = 1;
    }
    public Part(String shape, Color fcolor,Color bcolor, Boolean isfilled, int thick)
    {
        this.shape = shape;
        this.cords = new ArrayList<>();
        this.fcolor = fcolor;
        this.bcolor = bcolor;
        this.isfilled = isfilled;
        this.thick = thick;
    }
    public Part(String shape, ArrayList<Cord> cords, Color color, Boolean isfilled, int thick)
    {
        this.shape = shape;
        this.cords = cords;
        this.fcolor = color;
        this.bcolor = bcolor;
        this.isfilled = isfilled;
        this.thick = thick;
    }
}

class Cord 
{
    int x;
    int y;
    
    public Cord(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}
