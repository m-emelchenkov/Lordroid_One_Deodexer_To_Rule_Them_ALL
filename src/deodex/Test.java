/*
 * 
 * 
 * Copyright 2016 Rachid Boudjelida <rachidboudjelida@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package deodex;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
 
public class Test
{
    static JDialog dialog;
 
    public static void main(String[] args)
    {
        String[] options = { "yes", "no" };
        final JOptionPane op = new JOptionPane("message",
                                               JOptionPane.PLAIN_MESSAGE,
                                               JOptionPane.YES_NO_OPTION,
                                               null,
                                               options,
                                               "yes");
        final JFrame f = new JFrame();
        JButton show = new JButton("show option pane");
        show.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                showOptionPane(f, op);
            }
        });
        JPanel north = new JPanel();
        north.add(show);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(north, "North");
        f.setSize(300,200);
        f.setLocation(200,200);
        f.setVisible(true);
    }
 
    private static void showOptionPane(JFrame f, JOptionPane optionPane)
    {
        dialog = optionPane.createDialog(f, "title");
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.setVisible(true);
    }
}