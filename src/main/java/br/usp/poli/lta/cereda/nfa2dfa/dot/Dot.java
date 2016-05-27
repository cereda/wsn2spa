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
package br.usp.poli.lta.cereda.nfa2dfa.dot;

import br.usp.poli.lta.cereda.nfa2dfa.utils.SimpleTransition;
import br.usp.poli.lta.cereda.nfa2dfa.utils.Triple;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class Dot {
    
    private final StringBuilder sb;

    public Dot() {
        sb = new StringBuilder();
        sb.append("digraph finite_state_machine {").append("\n");
        sb.append("\t").append("rankdir=LR;").append("\n");
    }
    
    public void append(String name, String prefix, Triple<Integer, Set<Integer>,
                    List<SimpleTransition>> elements) {
        
        Set<Integer> states = new HashSet<>();   
        
        sb.append("\t").append(generateStart(name, prefix, elements.getFirst(),
                elements.getSecond().contains(elements.getFirst()))).
                append("\n");
        states.add(elements.getFirst());
        
        for (int state : elements.getSecond()) {
            if (!states.contains(state)) {
                states.add(state);
                sb.append("\t").append(createState(prefix, state, true)).
                        append("\n");
            }
        }
        
        for (SimpleTransition transition : elements.getThird()) {
            if (!states.contains(transition.getSource())) {
                states.add(transition.getSource());
                sb.append("\t").append(createState(prefix,
                        transition.getSource(), false)).append("\n");
            }
            
            if (!states.contains(transition.getTarget())) {
                states.add(transition.getTarget());
                sb.append("\t").append(createState(prefix,
                        transition.getTarget(), false)).append("\n");
            }
            
            sb.append("\t").append(createTransition(prefix,
                    transition)).append("\n");
        }
        
    }
    
    public void dump(String filename) {
        sb.append("}").append("\n");
        try {
            FileWriter fw = new FileWriter(new File(filename));
            fw.write(sb.toString());
            fw.close();
        }
        catch (IOException exception) {
            // do nothing
        }
    }
    
    private String createTransition(String prefix,
            SimpleTransition transition) {
        String pattern = "%s%d -> %s%d [ label = \"%s\" ];";
        String symbol;
        if (transition.epsilon()) {
            symbol = "ɛ";
        }
        else {
            symbol = transition.getSymbol().getValue();
        }
        return String.format(
                pattern,
                prefix,
                transition.getSource(),
                prefix,
                transition.getTarget(),
                symbol
                );
    }
    
    private String createState(String prefix, int state, boolean flag) {
        String pattern = "node [shape = %s, color=black, fontcolor=black, label=\"%d\" ]; %s%d;";
        String type = flag ? "doublecircle" : "circle";
        return String.format(
                pattern,
                type,
                state,
                prefix,
                state
                );
    }
    
    private String generateStart(String name, String prefix,
            int state, boolean flag) {
        String output = createState(prefix, state, flag);
        String start = "\tnode [shape = plaintext, color=white, fontcolor=black, label=\"%s\"]; start%s;";
        String edge = "start%s -> %s%d";
        return output.concat("\n\t").concat(String.format(start, name, prefix)).
                concat("\n\t").concat(String.format(edge, prefix,
                        prefix, state));
    }    
    
}
