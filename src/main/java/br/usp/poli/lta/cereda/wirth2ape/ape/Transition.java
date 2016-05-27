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
package br.usp.poli.lta.cereda.wirth2ape.ape;

import br.usp.poli.lta.cereda.wirth2ape.model.Token;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class Transition {

    private int source;
    private Token token;
    private int target;
    private String submachine;
    private int lookahead;
    private final List<Action> preActions;
    private final List<Action> postActions;

    public Transition(int source, Token token, int target) {
        this.source = source;
        this.token = token;
        this.target = target;
        this.submachine = null;
        this.lookahead = -1;
        this.preActions = new ArrayList<>();
        this.postActions = new ArrayList<>();
    }

    public Transition() {
        this.preActions = new ArrayList<>();
        this.postActions = new ArrayList<>();
    }

    public Transition(int source, String submachine, int target) {
        this.source = source;
        this.target = target;
        this.submachine = submachine;
        this.token = null;
        this.lookahead = -1;
        this.preActions = new ArrayList<>();
        this.postActions = new ArrayList<>();
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.submachine = null;
        this.token = token;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public String getSubmachine() {
        return submachine;
    }

    public void setSubmachine(String submachine) {
        this.token = null;
        this.submachine = submachine;
    }

    public void addPostAction(Action action) {
        postActions.add(action);
    }

    public void addPreAction(Action action) {
        preActions.add(action);
    }

    public void setLookahead(int lookahead) {
        this.lookahead = lookahead;
    }

    public int getLookahead() {
        return lookahead;
    }

    public boolean isSubmachineCall() {
        return submachine != null;
    }

    public List<Action> getPreActions() {
        return preActions;
    }

    public List<Action> getPostActions() {
        return postActions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transição: (").append("origem: ").append(source);
        sb.append(", ");
        if (isSubmachineCall()) {
            sb.append("submáquina: ").append(submachine);
        } else {
            sb.append("token: ").append(token);
        }
        sb.append(", destino: ").append(target);
        if (!preActions.isEmpty()) {
            sb.append(", ações anteriores: ").append("(");
            sb.append(StringUtils.join(preActions, ", "));
            sb.append(")");
        }
        if (!postActions.isEmpty()) {
            sb.append(", ações posteriores: ").append("(");
            sb.append(StringUtils.join(postActions, ", "));
            sb.append(")");
        }
        sb.append(")");
        return sb.toString();
    }

}
