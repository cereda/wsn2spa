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
package br.usp.poli.lta.cereda.nfa2dfa.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.yaml.snakeyaml.Yaml;

/**
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class Reader {
    
    private static String name;
    
    public static Triple<
        Integer,
         Set<Integer>,
         List<SimpleTransition>
        > read(String reference) throws Exception {

            Yaml yaml = new Yaml();
            Spec spec = yaml.loadAs(reference, Spec.class);
            name = spec.getName();
            
            List<SimpleTransition> transitions = new ArrayList<>();
            for (Transition t : spec.getTransitions()) {
                if (t.getSymbol() == null) {
                    transitions.add(
                            new SimpleTransition(
                                    t.getFrom(),
                                    t.getTo()
                            )
                    );
                }
                else {
                    transitions.add(
                            new SimpleTransition(
                                    t.getFrom(),
                                    new Token(
                                            t.getSymbol(),
                                            t.getSymbol()
                                    ),
                                    t.getTo()
                            )
                    );
                }
            }
            
            Triple<
                    Integer,
                    Set<Integer>,
                    List<SimpleTransition>
                    > result = new Triple<>();
            
            result.setFirst(spec.getInitial());
            result.setSecond(new HashSet<>(spec.getAccepting()));
            result.setThird(transitions);
            
            return result;
    }

    public static String getName() {
        return name == null ? "M" : name;
    }
   
}
