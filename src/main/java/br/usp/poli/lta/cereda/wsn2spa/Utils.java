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
package br.usp.poli.lta.cereda.wsn2spa;

import br.usp.poli.lta.cereda.nfa2dfa.utils.SimpleTransition;
import br.usp.poli.lta.cereda.nfa2dfa.utils.Triple;
import br.usp.poli.lta.cereda.wirth2ape.exporter.Spec;
import br.usp.poli.lta.cereda.wirth2ape.exporter.Transition;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

/**
 * 
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class Utils {

    public static void printBanner() {
        StringBuilder sb = new StringBuilder();
        sb.append("                ___               ").append('\n');
        sb.append("__ __ ______ _ |_  )____ __  __ _ ").append('\n');
        sb.append("\\ V  V (_-< ' \\ / /(_-< '_ \\/ _` |").append('\n');
        sb.append(" \\_/\\_//__/_||_/___/__/ .__/\\__,_|").append('\n');
        sb.append("                      |_|         ").append('\n');
        System.out.println(sb.toString());
    }

    public static Options getOptions() {
        Options options = new Options();
        options.addOption("o", "output", true, "DOT output");
        options.addOption("y", "yaml", true, "YAML output");
        options.addOption("c", "convert", false, "DFA conversion");
        options.addOption("m", "minimize", false, "state minimization");
        return options;
    }

    public static Spec toFormat(Triple<Integer, Set<Integer>,
            List<SimpleTransition>> spec) {
        Spec result = new Spec();
        result.setInitial(spec.getFirst());
        result.setAccepting(new ArrayList<>(spec.getSecond()));
        result.setTransitions(toTransitions(spec.getThird()));
        return result;
    }

    private static List<Transition> toTransitions(List<SimpleTransition>
            spec) {
        List<Transition> transitions = new ArrayList<>();
        spec.stream().map((simple) -> {
            Transition transition = new Transition();
            transition.setFrom(simple.getSource());
            transition.setTo(simple.getTarget());
            if (!simple.epsilon()) {
                transition.setSymbol(simple.getSymbol().getValue());
            }
            return transition;
        }).forEach((t) -> {
            transitions.add(t);
        });
        return transitions;
    }

    public static boolean required(CommandLine line, String... opts) {
        for (String opt : opts) {
            if (!line.hasOption(opt)) {
                return false;
            }
        }
        return true;
    }

    public static boolean neither(CommandLine line, String... opts) {
        for (String opt : opts) {
            if (line.hasOption(opt)) {
                return false;
            }
        }
        return true;
    }

    public static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("wsn2spa -o <pattern> -y"
                + " <pattern> [ -c ] [ -m ]", getOptions());
        System.exit(0);
    }

    public static void printException(Exception exception) {
        System.out.println(StringUtils.repeat("-", 70));
        System.out.println(StringUtils.center("An exception was thrown".
                toUpperCase(), 70));
        System.out.println(StringUtils.repeat("-", 70));
        System.out.println(WordUtils.wrap(exception.getMessage(),
                70, "\n", true));
        System.out.println(StringUtils.repeat("-", 70));
        System.exit(0);
    }

}
