package eu.euromov.activmotiv.controller;

import java.util.Optional;

import javax.crypto.SecretKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.SecurityContext;

import eu.euromov.activmotiv.authentication.ParticipantDetails;
import eu.euromov.activmotiv.model.Participant;
import eu.euromov.activmotiv.repository.ParticipantRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path="/participant")
public class ParticipantController {
	
	private Log log = LogFactory.getLog(ParticipantController.class);
	
	@Autowired
	ParticipantRepository participants;
	
	@Autowired
	SecretKey secretKey;
	
	@PostMapping(consumes = "application/json")
	public ResponseEntity<?> create(@Valid @RequestBody Participant participant) {
		Optional<Participant> found = participants.findById(participant.getUsername());
		// The given username needs to be already added without a password to be created
		if (found.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		else if (found.get().getPassword() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		participant.setPassword(encoder.encode(participant.getPassword()));
		participants.save(participant);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/login")
	public ResponseEntity<String> login(Authentication auth) {
		ParticipantDetails participant = (ParticipantDetails) auth.getPrincipal();
		ImmutableSecret<SecurityContext> secret = new ImmutableSecret<>(secretKey);
		NimbusJwtEncoder encoder = new NimbusJwtEncoder(secret);
		JwtClaimsSet claims = JwtClaimsSet.builder().claim("participant", participant.getUsername()).build();
		JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
		return ResponseEntity.ok(encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue());
	}
}