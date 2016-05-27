/**
 * ------------------------------------------------------
 *    Laboratório de Linguagens e Técnicas Adaptativas
 *       Escola Politécnica, Universidade São Paulo
 * ------------------------------------------------------
 * 
 * Copyright (c) 2016, Paulo Roberto Massa Cereda
 * 
 * Permission  is  hereby  granted, free  of  charge,  to
 * any  person  obtaining a  copy  of  this software  and
 * associated  documentation files  (the "Software"),  to
 * deal  in the  Software without  restriction, including
 * without limitation  the rights  to use,  copy, modify,
 * merge,  publish, distribute,  sublicense, and/or  sell
 * copies of the Software, and  to permit persons to whom
 * the Software  is furnished  to do  so, subject  to the
 * following conditions:
 * 
 * The above copyright notice  and this permission notice
 * shall  be  included  in   all  copies  or  substantial
 * portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED  "AS IS", WITHOUT WARRANTY OF
 * ANY  KIND,  EXPRESS  OR  IMPLIED,  INCLUDING  BUT  NOT
 * LIMITED TO THE  WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A  PARTICULAR PURPOSE  AND NONINFRINGEMENT.  IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES  OR OTHER LIABILITY, WHETHER IN
 * AN  ACTION OF  CONTRACT,  TORT  OR OTHERWISE,  ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package br.usp.poli.lta.cereda.wirth2ape.wirth;

import br.usp.poli.lta.cereda.wirth2ape.lexer.Lexer;
import br.usp.poli.lta.cereda.wirth2ape.model.Token;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class WirthLexer extends Lexer {

    private Set<String> words;

    public WirthLexer(String input) {
        super(input);
        words = new HashSet<>();
    }

    public void setWords(Set<String> words) {
        this.words = words;
    }

    @Override
    public Token recognize() {

        int state = 1;
        char symbol;
        boolean done = false;
        String value = "";
        String type = "";

        while (!done) {

            symbol = input.charAt(cursor);

            switch (state) {

                case 1:

                    if (!contains(symbol, ' ', '\t', '\n', '\r')) {

                        if (symbol == '"') {
                            type = "term";
                            value = "";
                            state = 4;
                        } else {
                            if (Character.isDigit(symbol)) {
                                type = "num";
                                value = String.valueOf(symbol);
                                state = 2;
                            } else {
                                if (Character.isLetter(symbol) &&
                                        symbol != 'ε') {
                                    type = "nterm";
                                    value = String.valueOf(symbol);
                                    state = 3;
                                } else {
                                    value = String.valueOf(symbol);
                                    type = String.valueOf(symbol);
                                    done = true;
                                }
                            }
                        }
                    }
                    cursor++;
                    break;

                case 2:

                    if (Character.isDigit(symbol)) {
                        value = value.concat(String.valueOf(symbol));
                        cursor++;
                    } else {
                        done = true;
                    }

                    break;

                case 3:

                    if (Character.isDigit(symbol) ||
                            Character.isLetter(symbol)) {
                        value = value.concat(String.valueOf(symbol));
                        cursor++;
                    } else {
                        done = true;
                    }

                    break;

                case 4:

                    if (symbol == '"') {
                        state = 1;
                    } else {
                        if (symbol == '\\') {
                            state = 5;
                        } else {
                            value = value.concat(String.valueOf(symbol));
                            state = 6;
                        }
                    }
                    cursor++;
                    break;

                case 5:

                    value = value.concat(String.valueOf(symbol));
                    state = 6;
                    cursor++;
                    break;

                case 6:

                    if (symbol == '\\') {
                        state = 5;
                    } else {
                        if (symbol == '"') {
                            done = true;
                        } else {
                            value = value.concat(String.valueOf(symbol));
                        }
                    }
                    cursor++;
                    break;

            }

            done = done || cursor == input.length();

        }

        if (value.isEmpty()) {
            return new Token();
        }

        if (type.equals("nterm")) {
            if (words.contains(value)) {
                type = "term";
            }
        }

        return new Token(type, value);
    }

    private boolean contains(char symbol, char... symbols) {
        for (char s : symbols) {
            if (symbol == s) {
                return true;
            }
        }
        return false;
    }

}
