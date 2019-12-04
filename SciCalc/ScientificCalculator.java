import java.lang.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ScientificCalculator extends JFrame {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel buttonsPanel;
	private final JTextField inputField;
	private JButton add, subtract, multiply, divide, raise, square, equals, decimalPoint, pie, clear;
	private JButton number1, number2, number3, number4, number5, number6, number7, number8, number9, number0;

	static String firstNumber = "0";
	static String secondNumber = "0";
	static double number = 0.0;
	static boolean secondValue = false;
	static boolean divisionPossible = false;
	static double value1 = 0;
	static double value2 = 0;
	static String currentOperation = "";
	static String cutArea = ",";

	public static void main(final String[] args) {
		new ScientificCalculator();
	}

	public ScientificCalculator() {
		setTitle("Calculator");
		setSize(320, 400);

		inputField = new JTextField(12);
		inputField.setFont(new Font("Times New Roman", Font.BOLD, 20));
		inputField.setHorizontalAlignment(JTextField.RIGHT);
		inputField.setEnabled(false);

		buttonsPanel = new JPanel();
		buttonsPanel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		setButtonsPanel(buttonsPanel);

		final Container pane = getContentPane();
		pane.setLayout(new GridLayout(2, 1));
		pane.add(inputField);
		pane.add(buttonsPanel);
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void setButtonsPanel(final JPanel buttonsPanel) {
		add = new JButton("+");
		subtract = new JButton("-");
		multiply = new JButton("*");
		divide = new JButton("/");
		raise = new JButton("^");
		square = new JButton("Square Root");
		equals = new JButton("=");
		decimalPoint = new JButton(".");
		pie = new JButton("pie");
		clear = new JButton("Clear");
		number1 = new JButton("1");
		number2 = new JButton("2");
		number3 = new JButton("3");
		number4 = new JButton("4");
		number5 = new JButton("5");
		number6 = new JButton("6");
		number7 = new JButton("7");
		number8 = new JButton("8");
		number9 = new JButton("9");
		number0 = new JButton("0");

		number7.addActionListener(new ButtonsHandler());
		number8.addActionListener(new ButtonsHandler());
		number9.addActionListener(new ButtonsHandler());
		number4.addActionListener(new ButtonsHandler());
		number5.addActionListener(new ButtonsHandler());
		number6.addActionListener(new ButtonsHandler());
		number1.addActionListener(new ButtonsHandler());
		number2.addActionListener(new ButtonsHandler());
		number3.addActionListener(new ButtonsHandler());
		decimalPoint.addActionListener(new ButtonsHandler());
		number0.addActionListener(new ButtonsHandler());
		clear.addActionListener(new ButtonsHandler());
		divide.addActionListener(new ButtonsHandler());
		multiply.addActionListener(new ButtonsHandler());
		subtract.addActionListener(new ButtonsHandler());
		add.addActionListener(new ButtonsHandler());
		square.addActionListener(new ButtonsHandler());
		raise.addActionListener(new ButtonsHandler());
		equals.addActionListener(new ButtonsHandler());

		pie.setEnabled(false);
		decimalPoint.setEnabled(false);
		add.setEnabled(false);
		divide.setEnabled(false);
		multiply.setEnabled(false);
		subtract.setEnabled(false);
		square.setEnabled(false);
		raise.setEnabled(false);
		equals.setEnabled(false);

		buttonsPanel.setLayout(new GridLayout(4, 5));
		buttonsPanel.add(number7);
		buttonsPanel.add(number8);
		buttonsPanel.add(number9);
		buttonsPanel.add(divide);
		buttonsPanel.add(square);
		buttonsPanel.add(number4);
		buttonsPanel.add(number5);
		buttonsPanel.add(number6);
		buttonsPanel.add(multiply);
		buttonsPanel.add(raise);
		buttonsPanel.add(number1);
		buttonsPanel.add(number2);
		buttonsPanel.add(number3);
		buttonsPanel.add(subtract);
		buttonsPanel.add(pie);
		buttonsPanel.add(decimalPoint);
		buttonsPanel.add(number0);
		buttonsPanel.add(clear);
		buttonsPanel.add(add);
		buttonsPanel.add(equals);
	}

	public class ButtonsHandler implements ActionListener {
		public void actionPerformed(final ActionEvent e) {

			if (e.getSource() == number7) {
				if (!secondValue)
					enableOperation();
				// checksDivPossible();
				inputField.setText(firstNumber + number7.getText());
				firstNumber = (inputField.getText());
			}

			if (e.getSource() == number8) {
				if (!secondValue)
					enableOperation();
				// checksDivPossible();
				inputField.setText(firstNumber + number8.getText());
				firstNumber = (inputField.getText());
			}

			if (e.getSource() == number9) {
				if (!secondValue)
					enableOperation();
				// checksDivPossible();
				inputField.setText(firstNumber + number9.getText());
				firstNumber = (inputField.getText());
			}

			if (e.getSource() == number4) {
				if (!secondValue)
					enableOperation();
				// checksDivPossible();
				inputField.setText(firstNumber + number4.getText());
				firstNumber = (inputField.getText());
			}

			if (e.getSource() == number5) {
				if (!secondValue)
					enableOperation();
				// checksDivPossible();
				inputField.setText(firstNumber + number5.getText());
				firstNumber = (inputField.getText());
			}

			if (e.getSource() == number6) {
				if (!secondValue)
					enableOperation();
				// checksDivPossible();
				inputField.setText(firstNumber + number6.getText());
				firstNumber = (inputField.getText());
			}

			if (e.getSource() == number1) {
				if (!secondValue)
					enableOperation();
				// checksDivPossible();
				inputField.setText(firstNumber + number1.getText());
				firstNumber = (inputField.getText());
			}

			if (e.getSource() == number2) {
				if (!secondValue)
					enableOperation();
				// checksDivPossible();
				inputField.setText(firstNumber + number2.getText());
				firstNumber = (inputField.getText());
			}

			if (e.getSource() == number3) {
				if (!secondValue)
					enableOperation();
				// checksDivPossible();
				inputField.setText(firstNumber + number3.getText());
				firstNumber = (inputField.getText());
			}

			if (e.getSource() == number0) {
				if (!secondValue)
					enableOperation();
				// checksDivPossible();
				inputField.setText(firstNumber + number0.getText());
				firstNumber = (inputField.getText());
			}

			if (e.getSource() == decimalPoint) {
				inputField.setText(firstNumber + ".");
				firstNumber = (inputField.getText());
				decimalPoint.setEnabled(false);
			}

			if (e.getSource() == clear) {
				disableOperation(1);
				inputField.setText("");
			}

			if (e.getSource() == square) {
				number = Math.sqrt(Double.parseDouble(firstNumber));
				inputField.setText("" + number);
				firstNumber = "";
			}

			if (e.getSource() == raise) {
				secondValue = true;
				currentOperation = "^";
				disableOperation(0);
				inputField.setText(firstNumber + "^");
				firstNumber = (inputField.getText());
			}

			if (e.getSource() == add) {
				secondValue = true;
				currentOperation = "+";
				disableOperation(0);
				inputField.setText(firstNumber + "+");
				firstNumber = (inputField.getText());
			}

			if (e.getSource() == subtract) {
				secondValue = true;
				currentOperation = "-";
				disableOperation(0);
				inputField.setText(firstNumber + "-");
				firstNumber = (inputField.getText());
			}

			if (e.getSource() == multiply) {
				secondValue = true;
				currentOperation = "*";
				disableOperation(0);
				inputField.setText(firstNumber + "*");
				firstNumber = (inputField.getText());
			}

			if (e.getSource() == divide) {
				secondValue = true;
				currentOperation = "/";
				// checksDivPossible();
				disableOperation(0);
				inputField.setText(firstNumber + "/");
				firstNumber = (inputField.getText());
			}

			if (e.getSource() == equals) {
				inputField.setText("" + result() + "");
				disableOperation(1);
			}

		}
	}

	public String determineOperation() {
		final String determinedOp = "";
		final String ops[] = { "+", "-", "/", "*", "^" };
		for (int j = 0; j < ops.length; j++) {
			try {

			} catch (final Exception x) {
			}
		}

		return determinedOp;
	}

	public void disableOperation(final int type) {
		if (type == 1) {
			firstNumber = "";
			currentOperation = "";
			secondValue = false;
		}

		decimalPoint.setEnabled(false);
		add.setEnabled(false);
		divide.setEnabled(false);
		multiply.setEnabled(false);
		subtract.setEnabled(false);
		square.setEnabled(false);
		raise.setEnabled(false);
		if (!secondValue)
			equals.setEnabled(false);
	}

	public void enableOperation() {
		decimalPoint.setEnabled(true);
		add.setEnabled(true);
		divide.setEnabled(true);
		multiply.setEnabled(true);
		subtract.setEnabled(true);
		square.setEnabled(true);
		raise.setEnabled(true);
		equals.setEnabled(true);
		equals.setEnabled(true);
	}

	public String result() {
		final String num[] = (inputField.getText().replace("^", ",").replace("+", ",").replace("-", ",")
				.replace("*", ",").replace("/", ",")).split(cutArea);
		String res = "";
		final double n1 = Double.parseDouble(num[0]);
		final double n2 = Double.parseDouble(num[1]);
		switch (currentOperation) {
		case "+": {
			res = "" + (n1 + n2);
		}
			;
			break;
		case "-": {
			res = "" + (n1 - n2);
		}
			;
			break;
		case "/": {
			res = "" + (n1 / n2);
		}
			;
			break;
		case "*": {
			res = "" + (n1 * n2);
		}
			;
			break;
		case "^": {
			res = "" + (Math.pow(n1, n2));
		}
			;
			break;
		}

		return res;
	}

}