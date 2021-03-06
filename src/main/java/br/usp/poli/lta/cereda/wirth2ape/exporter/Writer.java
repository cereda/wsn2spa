/**
* ------------------------------------------------------
*    Laboratório de Linguagens e Técnicas Adaptativas
*       Escola Politécnica, Universidade São Paulo
* ------------------------------------------------------
* 
* This program is free software: you can redistribute it
* and/or modify  it under the  terms of the  GNU General
* Public  License  as  published by  the  Free  Software
* Foundation, either  version 3  of the License,  or (at
* your option) any later version.
* 
* This program is  distributed in the hope  that it will
* be useful, but WITHOUT  ANY WARRANTY; without even the
* implied warranty  of MERCHANTABILITY or FITNESS  FOR A
* PARTICULAR PURPOSE. See the GNU General Public License
* for more details.
* 
**/
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
