import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class CoolScientificCalculator implements ActionListener {
    JFrame frame;
    String displayText = "";
    JLabel displayLabel;

    public CoolScientificCalculator() {
        frame = new JFrame("Cool Scientific Calculator");
        frame.setLayout(new BorderLayout());
        frame.setSize(400, 600);
        frame.setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new BorderLayout());

        displayLabel = new JLabel("");
        displayLabel.setBackground(Color.white);
        displayLabel.setForeground(Color.black);
        displayLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        displayLabel.setFont(new Font("Arial", Font.BOLD, 40));
        displayLabel.setOpaque(true);
        topPanel.add(displayLabel, BorderLayout.CENTER);

        JPanel controlButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton clearButton = new JButton("C");
        clearButton.setFont(new Font("Serif", Font.BOLD, 20));
        clearButton.addActionListener(this);
        controlButtonsPanel.add(clearButton);

        JButton backspaceButton = new JButton("Backspace");
        backspaceButton.setFont(new Font("Serif", Font.BOLD, 15));
        backspaceButton.addActionListener(this);
        controlButtonsPanel.add(backspaceButton);

        topPanel.add(controlButtonsPanel, BorderLayout.NORTH);

        frame.add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(6, 5, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] buttonLabels = { "7", "8", "9", "/", "(", "4", "5", "6", "x", ")", "1", "2", "3", "-", "sin", "0",
                ".", "=", "+", "cos", "π", "e", "tan", "^", "log" };

        for (String label : buttonLabels) {
            JButton btn = new JButton(label);
            btn.setFont(new Font("Serif", Font.BOLD, 20));
            buttonPanel.add(btn);
            btn.addActionListener(this);
        }

        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new CoolScientificCalculator();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source instanceof JButton) {
            JButton clickedButton = (JButton) source;
            String label = clickedButton.getText();

            if (label.equals("C")) {
                displayText = "";
            } else if (label.equals("Backspace")) {
                if (!displayText.isEmpty()) {
                    displayText = displayText.substring(0, displayText.length() - 1);
                }
            } else if (label.equals("=")) {
                if (!displayText.isEmpty()) {
                    try {
                        double result = evaluateExpression(displayText);
                        displayText = Double.toString(result);
                    } catch (ArithmeticException ex) {
                        displayText = "Error";
                    }
                }
            } else {
                displayText += label;
            }
            displayLabel.setText(displayText);
        }
    }

    private double evaluateExpression(String expression) throws ArithmeticException {
        // Step 1: Add '*' for implicit multiplication
        expression = expression.replaceAll("(\\d)([a-zA-Z()πe])", "$1*$2");
        expression = expression.replaceAll("([a-zA-Z()πe])(\\d)", "$1*$2");

        // Step 2: Handle the rest of the expressions
        // Custom evaluation logic for scientific functions and arithmetic operations
        if (expression.contains("sin")) {
            double angle = Double.parseDouble(expression.substring(3, expression.length() - 1));
            return Math.sin(Math.toRadians(angle));
        } else if (expression.contains("cos")) {
            double angle = Double.parseDouble(expression.substring(3, expression.length() - 1));
            return Math.cos(Math.toRadians(angle));
        } else if (expression.contains("tan")) {
            double angle = Double.parseDouble(expression.substring(3, expression.length() - 1));
            return Math.tan(Math.toRadians(angle));
        } else if (expression.contains("^")) {
            String[] tokens = expression.split("\\^");
            double base = Double.parseDouble(tokens[0]);
            double exponent = Double.parseDouble(tokens[1]);
            return Math.pow(base, exponent);
        } else if (expression.contains("√")) {
            double number = Double.parseDouble(expression.substring(1));
            return Math.sqrt(number);
        } else if (expression.equals("π")) {
            return Math.PI;
        } else if (expression.equals("e")) {
            return Math.E;
        } else if (expression.equals("log")) {
            return Math.log10(Double.parseDouble(displayText.substring(3)));
        } else {
            // For basic arithmetic operations
            String[] tokens = expression.split("(?=[-+x/])|(?<=[-+x/])");
            double result = Double.parseDouble(tokens[0]);
            for (int i = 1; i < tokens.length; i += 2) {
                String operator = tokens[i];
                double operand = Double.parseDouble(tokens[i + 1]);
                switch (operator) {
                    case "+":
                        result += operand;
                        break;
                    case "-":
                        result -= operand;
                        break;
                    case "x":
                        result *= operand;
                        break;
                    case "/":
                        if (operand == 0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        result /= operand;
                        break;
                    default:
                        throw new ArithmeticException("Invalid operator");
                }
            }
            return result;
        }
    }
}
