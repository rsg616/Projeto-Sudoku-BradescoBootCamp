import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Sudoku extends JFrame {
    private final int[][] board = new int[9][9];
    private final boolean[][] fixed = new boolean[9][9];
    private final JTextField[][] cells = new JTextField[9][9];

    public Sudoku() {
        setTitle("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLayout(new GridLayout(9, 9));

        // Sudoku inicial (exemplo)
        int[][] inicial = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        // Inicializa células
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cells[i][j] = cell;
                board[i][j] = inicial[i][j];
                if (inicial[i][j] != 0) {
                    cell.setText(String.valueOf(inicial[i][j]));
                    cell.setEditable(false);
                    cell.setBackground(new Color(220, 220, 220));
                    fixed[i][j] = true;
                } else {
                    cell.setDocument(new JTextFieldLimit(1));
                    final int row = i;
                    final int col = j;
                    cell.addActionListener(e -> verificarMovimento(row, col));
                }
                add(cell);
            }
        }

        setVisible(true);
    }

    private void verificarMovimento(int linha, int coluna) {
        String texto = cells[linha][coluna].getText();
        if (!texto.matches("[1-9]")) {
            JOptionPane.showMessageDialog(this, "Digite um número entre 1 e 9.");
            cells[linha][coluna].setText("");
            return;
        }

        int valor = Integer.parseInt(texto);
        if (movimentoValido(linha, coluna, valor)) {
            board[linha][coluna] = valor;
            if (jogoCompleto()) {
                JOptionPane.showMessageDialog(this, "Parabéns! Sudoku completo!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Movimento inválido.");
            cells[linha][coluna].setText("");
        }
    }

    private boolean movimentoValido(int linha, int coluna, int valor) {
        for (int i = 0; i < 9; i++) {
            if (board[linha][i] == valor || board[i][coluna] == valor)
                return false;
        }

        int boxRow = (linha / 3) * 3;
        int boxCol = (coluna / 3) * 3;
        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (board[i][j] == valor)
                    return false;
            }
        }

        return true;
    }

    private boolean jogoCompleto() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (board[i][j] == 0)
                    return false;
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Sudoku::new);
    }
}

// Classe auxiliar para limitar texto a 1 caractere
class JTextFieldLimit extends javax.swing.text.PlainDocument {
    private final int limit;

    JTextFieldLimit(int limit) {
        this.limit = limit;
    }

    public void insertString(int offset, String str, javax.swing.text.AttributeSet attr)
            throws javax.swing.text.BadLocationException {
        if (str == null) return;
        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}
