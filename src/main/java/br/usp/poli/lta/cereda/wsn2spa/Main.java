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

import br.usp.poli.lta.cereda.nfa2dfa.utils.Conversion;
import br.usp.poli.lta.cereda.nfa2dfa.utils.Reader;
import br.usp.poli.lta.cereda.nfa2dfa.utils.SimpleTransition;
import br.usp.poli.lta.cereda.nfa2dfa.utils.Triple;
import br.usp.poli.lta.cereda.wirth2ape.exporter.Spec;
import br.usp.poli.lta.cereda.wirth2ape.exporter.Writer;
import br.usp.poli.lta.cereda.wirth2ape.wirth.Generator;
import br.usp.poli.lta.cereda.wirth2ape.wirth.WirthLexer;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

/**
 * 
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class Main {

    public static void main(String[] args) {        
        
        Utils.printBanner();
        CommandLineParser parser = new DefaultParser();

        try {

            CommandLine line = parser.parse(Utils.getOptions(), args);

            if (!Utils.required(line, "o", "y") || line.getArgs().length != 1) {
                throw new Exception("Note that 'o' and 'y' flags are required"
                        + " to run this tool, as they generate DOT and YAML"
                        + " files, respectively. Also, do not forget to include"
                        + " the replacement pattern '%s' in order to generate"
                        + " files corresponding to each submachine in the"
                        + " automaton model.");
            }

            if (!line.getOptionValue("o").contains("%s")
                    || !line.getOptionValue("y").contains("%s")) {
                throw new Exception("Flags 'o' and 'y' lack the replacement"
                        + " pattern '%s' in order to generate files"
                        + " corresponding to each submachine in the automaton"
                        + " model.");
            }

            File file = new File(line.getArgs()[0]);
            if (!file.exists()) {
                throw new Exception("The provided grammar file '" + "' does"
                        + " not exist. Make sure the location is correct and"
                        + " try again.");
            }

            String text = FileUtils.readFileToString(file, "UTF-8").trim();
            WirthLexer wl = new WirthLexer(text);
            Generator g = new Generator(wl);
            g.generateAutomaton();

            Writer writer = new Writer(g.getTransitions());
            Map<String, String> map =
                    writer.generateYAMLMap(line.getOptionValue("y"));

            if (Utils.neither(line, "c", "m")) {
                br.usp.poli.lta.cereda.wirth2ape.dot.Dot dot =
                        new br.usp.poli.lta.cereda.wirth2ape.dot.Dot(
                                g.getTransitions()
                        );
                dot.generate(line.getOptionValue("o"));
                for (String key : map.keySet()) {
                    FileUtils.write(new File(key), map.get(key), "UTF-8");
                }
            } else {
                System.out.println("Additional operations:");
                if (line.hasOption("c")) {
                    System.out.println("- Submachines translated to DFA's.");
                }
                if (line.hasOption("m")) {
                    System.out.println("- State minimization applied.");
                }

                for (String key : map.keySet()) {

                    Triple<Integer, Set<Integer>, List<SimpleTransition>> spec =
                            Reader.read(map.get(key));
                    br.usp.poli.lta.cereda.nfa2dfa.dot.Dot dot =
                            new br.usp.poli.lta.cereda.nfa2dfa.dot.Dot();
                    dot.append(Reader.getName(), "original", spec);

                    Conversion c;

                    if (line.hasOption("c")) {
                        c = new Conversion(spec.getThird(), spec.getFirst(),
                                spec.getSecond());
                        spec = c.convert();
                        dot.append(Reader.getName().concat("'"),
                                "converted", spec);
                    }

                    if (line.hasOption("m")) {
                        if (!line.hasOption("c")) {
                            throw new Exception("State minimization cannot be"
                                    + "applied if the DFA conversion was not"
                                    + "specified. Make sure to include the"
                                    + "'-c' flag as well and try again.");
                        }
                        c = new Conversion(spec.getThird(), spec.getFirst(),
                                spec.getSecond());
                        spec = c.minimize();
                        dot.append(Reader.getName().concat("''"), "minimized",
                                spec);
                    }

                    Yaml yaml = new Yaml();
                    Spec result = Utils.toFormat(spec);
                    result.setName(Reader.getName());
                    map.put(key, yaml.dump(result));

                    String dotname = String.format(line.getOptionValue("o"),
                            Reader.getName());
                    dot.dump(dotname);

                }

                for (String key : map.keySet()) {
                    FileUtils.write(new File(key), map.get(key), "UTF-8");
                }
            }
            
            System.out.println("Done.");
        } catch (ParseException nothandled) {
            Utils.printHelp();
        } catch (Exception exception) {
            Utils.printException(exception);
        }
    }

}