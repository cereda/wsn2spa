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
package br.usp.poli.lta.cereda.wirth2ape.exporter;

import br.usp.poli.lta.cereda.wirth2ape.ape.conversion.Sketch;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class Writer {
    
    private final List<Sketch> transitions;

    public Writer(List<Sketch> transitions) {
        this.transitions = transitions;
    }

    public Map<String, String> generateYAMLMap(String filename)
            throws Exception {
        Map<String, List<Sketch>> map = new HashMap<>();
        transitions.stream().map((sketch) -> {
            if (!map.containsKey(sketch.getName())) {
                map.put(sketch.getName(), new ArrayList<>());
            }
            return sketch;
        }).forEach((sketch) -> {
            map.get(sketch.getName()).add(sketch);
        });
        
        String output;
        Map<String, String> result = new HashMap<>();
        for (String machine : map.keySet()) {
            output = String.format(filename, machine);
            result.put(output, write(machine, map.get(machine)));
        }
        return result;
        
    }
    
    private String write(String name, List<Sketch> sketches)
            throws Exception {
        
        Spec spec = new Spec();
        spec.setName(name);
        spec.setInitial(0);
        spec.setAccepting(Arrays.asList(1));
        List<Transition> ts = new ArrayList<>();
        sketches.stream().map((sketch) -> {
            Transition t = new Transition();
            t.setFrom(sketch.getSource());
            t.setTo(sketch.getTarget());
            if (!sketch.epsilon()) {
                if (sketch.call()) {
                    t.setSymbol(sketch.getSubmachine().concat(" (call)"));
                }
                else {
                    t.setSymbol(sketch.getToken().getValue());
                }
            }
            return t;
        }).forEach((t) -> {
            ts.add(t);
        });
        spec.setTransitions(ts);
        Yaml yaml = new Yaml();
        return yaml.dump(spec);
    }   
    
}
