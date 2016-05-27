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

import java.util.List;

/**
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class Spec {

    private String name;
    private int initial;
    private List<Transition> transitions;
    private List<Integer> accepting;

    public int getInitial() {
        return initial;
    }

    public void setInitial(int initial) {
        this.initial = initial;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    public List<Integer> getAccepting() {
        return accepting;
    }

    public void setAccepting(List<Integer> accepting) {
        this.accepting = accepting;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
