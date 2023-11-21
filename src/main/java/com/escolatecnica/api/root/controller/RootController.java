package com.escolatecnica.api.root.controller;

import com.escolatecnica.api.Main;
import lombok.AllArgsConstructor;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;
import java.io.IOException;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class RootController {
    @GetMapping
    public ResponseEntity<?> publicEndpoint() {
        return new ResponseEntity<>(new Root(Main.START_UP_TIME, "ETERNA - API"), HttpStatus.OK);
    }

    private static class Root {

        public ZonedDateTime startup;
        public String message;
        public String version;

        public Root(ZonedDateTime startup, String message) {
            this.startup = startup;
            this.message = message;
            this.version = version();
        }

        public String version() {
            try {
                MavenXpp3Reader reader = new MavenXpp3Reader();
                return reader.read(new FileReader("pom.xml")).getVersion();
            } catch (IOException | XmlPullParserException e) {
                return "?";
            }
        }
    }


}
