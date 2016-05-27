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

import br.usp.poli.lta.cereda.wirth2ape.ape.Action;
import br.usp.poli.lta.cereda.wirth2ape.ape.StructuredPushdownAutomaton;
import br.usp.poli.lta.cereda.wirth2ape.ape.Transition;
import br.usp.poli.lta.cereda.wirth2ape.ape.conversion.Sketch;
import br.usp.poli.lta.cereda.wirth2ape.model.Token;
import br.usp.poli.lta.cereda.wirth2ape.structure.Stack;
import br.usp.poli.lta.cereda.wirth2ape.tuple.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class Generator {
    
    private final WirthLexer lexer;
    private final Stack<Pair<Integer, Integer>> helper;
    private final List<Sketch> transitions;
    private int current;
    private int counter;
    private String main;
    private String machine;
    
    
    public Generator(WirthLexer lexer) {
        this.lexer = lexer;
        this.helper = new Stack<>();
        this.transitions = new ArrayList<>();
    }
    
    public void generateAutomaton() throws Exception {
        
        StructuredPushdownAutomaton spa = new StructuredPushdownAutomaton();
        
        spa.setSubmachine("GRAM");
        
        spa.addSubmachine("GRAM", 1, getSet(5));
        spa.addSubmachine("EXPR", 6, getSet(7));
        
        Action criarNovaSubmaquina = new Action("criarNovaSubmaquina") { 
            @Override
            public void execute(Token token) {
                helper.clear();
                current = 0;
                counter = 1;
                machine = token.getValue();
                if (main == null) {
                    main = machine;
                }
            }

            @Override
            public List execute(int state, List tree) {
                return null;
            }
        };
        
        Action novaTransicao = new Action("novaTransicao") {   
            @Override
            public void execute(Token token) {
                Sketch transition;
                if (token.getType().equals("nterm")) {
                    transition = new Sketch(machine, current,
                            token.getValue(), counter);
                }
                else {
                    if (token.getType().equals("ε")) {
                        transition = new Sketch(machine, current, counter);
                    }
                    else {
                        transition = new Sketch(machine, current,
                                token, counter);
                    }
                }
                transitions.add(transition);
                current = counter;
                counter++;
            }
            
            @Override
            public List execute(int state, List tree) {
                return null;
            }
        };
        
        Action novoEscopo = new Action("novoEscopo") {   
            @Override
            public void execute(Token token) {
                helper.push(new Pair<>(current, counter));
                counter++;
            }
            
            @Override
            public List execute(int state, List tree) {
                return null;
            }
        };
        
        Action abreColchetes = new Action("abreColchetes") {  
            @Override
            public void execute(Token token) {
                Sketch transition = new Sketch(machine, current, counter);
                transitions.add(transition);
                helper.push(new Pair<>(current, counter));
                counter++;
            }
            
            @Override
            public List execute(int state, List tree) {
                return null;
            }
        };
        
        Action abreChaves = new Action("abreChaves") {  
            @Override
            public void execute(Token token) {
                Sketch transition = new Sketch(machine, current, counter);
                transitions.add(transition);
                current = counter;
                helper.push(new Pair<>(counter, counter));
                counter++;
            }
            
            @Override
            public List execute(int state, List tree) {
                return null;
            }
        };
        
        Action fechaEscopo = new Action("fechaEscopo") {  
            @Override
            public void execute(Token token) {
                Sketch transition = new Sketch(machine, current,
                        helper.top().getSecond());
                transitions.add(transition);
                current = helper.top().getSecond();
                helper.pop();
            }
            
            @Override
            public List execute(int state, List tree) {
                return null;
            }
        };
        
        Action adicionaOpcao = new Action("adicionaOpcao") {   
            @Override
            public void execute(Token token) {
                Sketch transition = new Sketch(machine, current,
                        helper.top().getSecond());
                transitions.add(transition);
                current = helper.top().getFirst();
            }
            
            @Override
            public List execute(int state, List tree) {
                return null;
            }
        };
        
        Transition t1 = new Transition(1, new Token("nterm", "nterm"), 2);
        t1.addPreAction(criarNovaSubmaquina);
        spa.addTransition(t1);
        
        Transition t2 = new Transition(2, new Token("=", "="), 3);
        t2.addPreAction(novoEscopo);
        spa.addTransition(t2);
        
        Transition t3 = new Transition(3, "EXPR", 4);
        spa.addTransition(t3);
        
        Transition t4 = new Transition(4, new Token(".", "."), 5);
        t4.addPreAction(fechaEscopo);
        spa.addTransition(t4);
        
        Transition t5 = new Transition(5, new Token("nterm", "nterm"), 2);
        t5.addPreAction(criarNovaSubmaquina);
        spa.addTransition(t5);
        
        Transition t6 = new Transition(6, new Token("nterm", "nterm"), 7);
        t6.addPreAction(novaTransicao);
        spa.addTransition(t6);
        
        Transition t7 = new Transition(6, new Token("term", "term"), 7);
        t7.addPreAction(novaTransicao);
        spa.addTransition(t7);
        
        Transition t8 = new Transition(6, new Token("ε", "ε"), 7);
        t8.addPreAction(novaTransicao);
        spa.addTransition(t8);
        
        Transition t9 = new Transition(7, new Token("nterm", "nterm"), 7);
        t9.addPreAction(novaTransicao);
        spa.addTransition(t9);
        
        Transition t10 = new Transition(7, new Token("term", "term"), 7);
        t10.addPreAction(novaTransicao);
        spa.addTransition(t10);
        
        Transition t11 = new Transition(7, new Token("ε", "ε"), 7);
        t11.addPreAction(novaTransicao);
        spa.addTransition(t11);
        
        Transition t12 = new Transition(6, new Token("(", "("), 8);
        t12.addPreAction(novoEscopo);
        spa.addTransition(t12);
        
        Transition t13 = new Transition(6, new Token("[", "["), 10);
        t13.addPreAction(abreColchetes);
        spa.addTransition(t13);
        
        Transition t14 = new Transition(6, new Token("{", "{"), 12);
        t14.addPreAction(abreChaves);
        spa.addTransition(t14);
        
        Transition t15 = new Transition(7, new Token("(", "("), 8);
        t15.addPreAction(novoEscopo);
        spa.addTransition(t15);
        
        Transition t16 = new Transition(7, new Token("[", "["), 10);
        t16.addPreAction(abreColchetes);
        spa.addTransition(t16);
        
        Transition t17 = new Transition(7, new Token("{", "{"), 12);
        t17.addPreAction(abreChaves);
        spa.addTransition(t17);
        
        Transition t18 = new Transition(8, "EXPR", 9);
        spa.addTransition(t18);
        
        Transition t19 = new Transition(10, "EXPR", 11);
        spa.addTransition(t19);
        
        Transition t20 = new Transition(12, "EXPR", 13);
        spa.addTransition(t20);
        
        Transition t21 = new Transition(9, new Token(")", ")"), 7);
        t21.addPreAction(fechaEscopo);
        spa.addTransition(t21);
        
        Transition t22 = new Transition(11, new Token("]", "]"), 7);
        t22.addPreAction(fechaEscopo);
        spa.addTransition(t22);
        
        Transition t23 = new Transition(13, new Token("}", "}"), 7);
        t23.addPreAction(fechaEscopo);
        spa.addTransition(t23);
        
        Transition t24 = new Transition(7, new Token("|", "|"), 6);
        t24.addPreAction(adicionaOpcao);
        spa.addTransition(t24);
        
        spa.setup();
        
        boolean result = spa.parse(lexer);
        
        if (!result) {
            throw new Exception("There were errors while trying to perform "
                    + "lexical analysis on the provided file. Make sure the"
                    + "grammar is correct (valid Wirth syntax notation) and"
                    + "try again.");
        }
        
    }
    
    private <T> Set<T> getSet(T... elements) {
        Set<T> result = new HashSet<>();
        result.addAll(Arrays.asList(elements));
        return result;
    }

    public List<Sketch> getTransitions() {
        return transitions;
    }
  
}
