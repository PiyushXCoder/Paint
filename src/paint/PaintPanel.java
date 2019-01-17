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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

/**
 *
 * @author piyush
 */
public class PaintPanel extends Component implements MouseMotionListener, MouseListener
{
    public ArrayList<Part> parts = new ArrayList<>();
    public Color color = Color.BLACK;
    public String shape = "pencil";
    public Boolean isfilled = false;
    
    public PaintPanel()
    {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        
    }
        
    @Override
    public void paint(Graphics g)
    {
        for(Part part : parts)
        {
            g.setColor(part.color);
            if(part.shape.equals("pencil"))
            {
                int x = part.cords.get(0).x;
                int y = part.cords.get(0).y;


                for(int i = 1; i < part.cords.size(); i++)
                {
                    g.drawLine(x, y, part.cords.get(i).x, part.cords.get(i).y);
                    x = part.cords.get(i).x;
                    y = part.cords.get(i).y;
                }
            }
            else if(part.shape.equals("line"))
            {
                if(part.cords.size() > 1) g.drawLine(part.cords.get(0).x, part.cords.get(0).y, part.cords.get(1).x, part.cords.get(1).y);
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
                    if(part.isfilled) g.fillRect(x1,y1,x2 - x1,y2 - y1);
                    else g.drawRect(x1,y1,x2 - x1,y2 - y1);
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
                    if(part.isfilled) g.fillOval(x1,y1,x2 - x1,y2 - y1);
                    else g.drawOval(x1,y1,x2 - x1,y2 - y1);
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
                    if(part.isfilled) g.fillOval(part.cords.get(0).x - r,part.cords.get(0).y - r,2 * r,2 * r);
                    else g.drawOval(part.cords.get(0).x - r,part.cords.get(0).y - r,2 * r,2 * r);
                }
            }
        }
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
        parts.add(new Part(shape, color,isfilled));
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
    Color color;
    Boolean isfilled;
    
    public Part()
    {
        this.shape = "pencil";
        this.cords = new ArrayList<>();
        this.color = Color.black;
        this.isfilled = false;
    }
    public Part(String shape, Color color, Boolean isfilled)
    {
        this.shape = shape;
        this.cords = new ArrayList<>();
        this.color = color;
        this.isfilled = isfilled;
    }
    public Part(String shape, ArrayList<Cord> cords, Color color, Boolean isfilled)
    {
        this.shape = shape;
        this.cords = cords;
        this.color = color;
        this.isfilled = isfilled;
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
