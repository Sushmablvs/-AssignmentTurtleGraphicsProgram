import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import uk.ac.leedsbeckett.oop.OOPGraphics;



class GraphicsSystem extends OOPGraphics {

    private BufferedImage canvas;
    private ArrayList<String> commandsList;

    private boolean isImageSaved = false;



    public GraphicsSystem() {
        canvas = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        commandsList = new ArrayList<>();

    }

    public void setupGraphics() {
        JFrame mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new FlowLayout());
        mainFrame.add(this);
        mainFrame.pack();
        mainFrame.setVisible(true);
        //super.about();
    }

    public void processCommand(String command) {
        String[] parts = command.trim().split(" ");
        String commandType = parts[0].toLowerCase();

        switch (commandType) {
            case "about":
                about();
                break;
            case "reset":
                reset();
                setPenColour(Color.RED);
                setStroke(1);
                break;
            case "pendown":
                penDown();
                break;
            case "forward":
                handleForwardCommand(parts);
                break;
            case "backward":
                handleBackwardCommand(parts);
                break;
            case "turnleft":
                handleTurnLeftCommand(parts);
                break;
            case "turnright":
                handleTurnRightCommand(parts);
                break;
            case "penup":
                penUp();
                break;
            case "red":
                setPenColour(Color.RED);
                break;
            case "green":
                setPenColour(Color.GREEN);
                break;
            case "blue":
                setPenColour(Color.BLUE);
                break;
            case "yellow":
                setPenColour(Color.YELLOW);
                break;
            case "clear":
                if (isImageSaved) {
                    clear();
                    isImageSaved = false;
                    break;
                }
                else {
                    int result = JOptionPane.showConfirmDialog(null, "The current image is not saved. Are you sure you want to clear the canvas?", "Warning", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.NO_OPTION) {
                        break;
                    }
                    else{
                        clear();
                        break;
                    }
                }
            case "right":
                turnRight(90);
                break;

            case "save":
                saveImage();
                break;

            case "savecommands":

                saveCommands();
                break;

            case "load":
                loadImage();
                break;
            case "loadcommands":
                loadCommands();
                break;
            case "square":
                handlesquareCommand(parts);
                break;
            case "triangle":
                handletriangleCommand(parts);
                break;
            case "pencolor":
                handlepenCommand(parts);
                break;
            case "triangle_3sides":
                handletriangle(parts);
                break;



            case "penwidth":
                handlePenWidthCommand(parts);
                break;

            default:
                JOptionPane.showMessageDialog(null, "Invalid Command: " + command);
        }

        commandsList.add(command);
    }








    private void handleForwardCommand(String[] parts) {
        if (parts.length == 2) {
            try {
                int distance = Integer.parseInt(parts[1]);
                if (isValidDistance(distance)) {
                    forward(distance);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid distance: " + distance);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid Parameter for the command Non Numeric: " + parts[0]);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Missing Parameter for the command: " + parts[0]);
        }
    }

    private void handleBackwardCommand(String[] parts) {
        if (parts.length == 2) {
            try {
                int distance = Integer.parseInt(parts[1]);
                if (isValidDistance(distance)) {
                    forward(-distance);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid distance: " + distance);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid Parameter for the command: " + parts[0]);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Missing Parameter for the command: " + parts[0]);
        }
    }
    private boolean isValidDistance(int distance) {
        int MAX_DISTANCE = 1000;
        return distance >= 0 && distance <= MAX_DISTANCE;
    }

    private boolean isValidDegrees(int degrees) {
        int MAX_DEGREES = 360;
        return degrees >= 0 && degrees <= MAX_DEGREES;
    }
    private void handleTurnLeftCommand(String[] parts) {
        if (parts.length == 2) {
            try {
                int degrees = Integer.parseInt(parts[1]);
                if (isValidDegrees(degrees)) {
                    turnLeft(degrees);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid degrees: " + degrees);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid Parameter for the command: " + parts[0]);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Missing Parameter for the command: " + parts[0]);
        }
    }

    private void handleTurnRightCommand(String[] parts) {
        if (parts.length == 2) {
            try {
                int degrees = Integer.parseInt(parts[1]);
                if (isValidDegrees(degrees)) {
                    turnRight(degrees);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid degrees: " + degrees);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid Parameter for the command: " + parts[0]);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Missing Parameter for the command: " + parts[0]);
        }
    }


    private void handletriangleCommand(String[] parts) {
        if (parts.length == 2) {
            try {
                int distance = Integer.parseInt(parts[1]);
                if (isValidDistance(distance)) {
                    triangle(distance); // Note: This should be backward, not forward
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid distance: " + distance);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid Parameter for the command: " + parts[0]);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Missing Parameter for the command: " + parts[0]);
        }
    }

    private void handlePenWidthCommand(String[] parts) {
        if (parts.length == 2) {
            try {
                int width = Integer.parseInt(parts[1]);
                setStroke(width);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid parameter for penwidth command.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid command format for penwidth.");
        }
    }

    private void handlesquareCommand(String[] parts) {
        if (parts.length == 2) {
            try {
                int distance = Integer.parseInt(parts[1]);
                if (isValidDistance(distance)) {
                    square(distance); // Note: This should be backward, not forward
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid distance: " + distance);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid Parameter for the command: " + parts[0]);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Missing Parameter for the command: " + parts[0]);
        }
    }


    private void saveImage() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                Graphics2D g2d = canvas.createGraphics();
                paintComponent(g2d);
                g2d.dispose();
                ImageIO.write(canvas, "jpg", file);
                JOptionPane.showMessageDialog(null, "Image saved successfully!");
                isImageSaved = true;
                BufferedImage savedImage = ImageIO.read(file);
                JLabel imageLabel = new JLabel(new ImageIcon(savedImage));
                JOptionPane.showMessageDialog(null, imageLabel);

            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving image: " + e.getMessage());
        }
    }

    private void loadImage() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (file != null && file.exists()) {
                    BufferedImage img = ImageIO.read(file);
                    if (img != null) {
                        canvas = img;
                        setPreferredSize(new Dimension(canvas.getWidth(), canvas.getHeight()));

                        // Repaint the canvas to display the loaded image
                        repaint();
                        JOptionPane.showMessageDialog(null, "Image loaded successfully!");

                        // Draw the loaded image onto the canvas
                        Graphics g = getGraphics();
                        g.drawImage(canvas, 0, 0, null);
                        g.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: Unable to load image. The file may be corrupted or in an unsupported format.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Selected file does not exist.");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading image: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }




    private void saveCommands() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                FileWriter writer = new FileWriter(file);
                for (String command : commandsList) {
                    writer.write(command + "\n");
                }
                writer.close();
                JOptionPane.showMessageDialog(null, "Commands saved successfully!");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving commands: " + e.getMessage());
        }
    }

    private void loadCommands() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    processCommand(line);
                }
                reader.close();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading commands: " + e.getMessage());
        }
    }

    @Override
    public void about() {
        super.about();
        Graphics g = getGraphics2DContext();
        g.setColor(Color.green);
        g.drawString("Lalitha Valli Sai Sushma Bokka", 225, 300);
    }




    public void square(int length) {
        forward(length);
        turnRight(90);
        forward(length);
        turnRight(90);
        forward(length);
        turnRight(90);
        forward(length);
        reset();
    }

    public void triangle(int size) {
        double angle = Math.toRadians(30);
        int height = (int) (Math.sin(angle) * size);
        int base = size * 2;
        forward(size);
        turnLeft(120);
        forward(size);
        turnLeft(120);
        forward(size);
        turnLeft(120);
        reset();
    }

    public void handlepenCommand(String[] parts) {
        if (parts.length == 4) {
            try {
                int red = Integer.parseInt(parts[1].trim());
                int green = Integer.parseInt(parts[2].trim());
                int blue = Integer.parseInt(parts[3].trim());

                if (isValidRGB(red) && isValidRGB(green) && isValidRGB(blue)) {
                    setPenColour(new Color(red, green, blue));
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid RGB values: Values must be between 0 and 255");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid RGB values: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid command format: ");
        }
    }

    private boolean isValidRGB(int value) {
        return value >= 0 && value <= 255;
    }



    private void handletriangle(String[] parts) {
        if (parts.length == 4) {
            try {
                int side1 = Integer.parseInt(parts[1]);
                int side2 = Integer.parseInt(parts[2]);
                int side3 = Integer.parseInt(parts[3]);

                // Check if the triangle sides satisfy the triangle inequality theorem
                if (isValidTriangle(side1, side2, side3)) {
                    // Calculate the angles of the triangle using the law of cosines
                    double angle1 = Math.acos((side2 * side2 + side3 * side3 - side1 * side1) / (2.0 * side2 * side3));
                    double angle2 = Math.acos((side1 * side1 + side3 * side3 - side2 * side2) / (2.0 * side1 * side3));
                    double angle3 = Math.acos((side1 * side1 + side2 * side2 - side3 * side3) / (2.0 * side1 * side2));

                    // Convert angles from radians to degrees
                    angle1 = Math.round(Math.toDegrees(angle1));
                    angle2 = Math.round(Math.toDegrees(angle2));
                    angle3 = Math.round(Math.toDegrees(angle3));

                    // Draw the triangle
                    forward(side1); // Move forward by side1
                    turnLeft((int)(180-angle3)); // Turn left by angle2
                    forward(side2); // Move forward by side2
                    turnLeft((int)(180 - angle1)); // Turn left by angle3
                    forward(side3); // Move forward by side3

                    // Reset the turtle
                    reset();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid triangle sides: " + side1 + ", " + side2 + ", " + side3);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid Parameters for the command: " + parts[0]);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid command format: ");
        }
    }

    private boolean isValidTriangle(int side1, int side2, int side3) {
        // Check if the sum of the lengths of any two sides of the triangle is greater than the length of the third side
        return side1 + side2 > side3 && side1 + side3 > side2 && side2 + side3 > side1;
    }



}

public class Main {
    public static void main(String[] args) {
        GraphicsSystem graphicsSystem = new GraphicsSystem();
        graphicsSystem.setupGraphics();
    }
}

