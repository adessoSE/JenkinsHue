package de.adesso.jenkinshue.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.adesso.jenkinshue.common.dto.lamp.LampCreateDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampGroupedScenariosDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampHueDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampNameDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampRenameDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampTestDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampTurnOffDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampUpdateDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampUpdateLastShownScenarioDTO;
import de.adesso.jenkinshue.common.dto.team.TeamLampsDTO;
import de.adesso.jenkinshue.common.service.LampService;
import de.adesso.jenkinshue.exception.InvalidWorkingPeriodException;
import de.adesso.jenkinshue.exception.LampAlreadyExistsException;
import de.adesso.jenkinshue.exception.LampDoesNotExistsException;
import de.adesso.jenkinshue.exception.TeamDoesNotExistException;

/**
 *
 * @author wennier
 *
 */
@RestController
@RequestMapping("/rest/lamps")
public class LampController implements LampService {

	private final LampService lampService;

	@Autowired
	public LampController(LampService lampService) {
		this.lampService = lampService;
	}

	@Override
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public LampDTO create(@RequestBody @Valid LampCreateDTO lamp) throws LampAlreadyExistsException {
		return lampService.create(lamp);
	}

	@Override
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public LampDTO update(@RequestBody @Valid LampUpdateDTO lamp) throws InvalidWorkingPeriodException, LampDoesNotExistsException {
		return lampService.update(lamp);
	}
	
	@Override
	public LampDTO updateLastShownScenario(LampUpdateLastShownScenarioDTO lamp) throws LampDoesNotExistsException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	@RequestMapping(value = "/rename", method = RequestMethod.POST)
	public LampDTO rename(@RequestBody @Valid LampRenameDTO lamp) throws LampDoesNotExistsException {
		return lampService.rename(lamp);
	}
	
	@Override
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.DELETE)
	public void remove(@PathVariable long id) {
		lampService.remove(id);
	}

	@Override
	@RequestMapping
	public List<LampDTO> findAll() {
		return lampService.findAll();
	}
	
	@Override
	@RequestMapping("/{id}")
	public LampGroupedScenariosDTO findOne(@PathVariable long id) throws LampDoesNotExistsException {
		return lampService.findOne(id);
	}

	@Override
	@RequestMapping("/count")
	public long count() {
		return lampService.count();
	}
	
	@Override
	public long count(long teamId) {
		throw new UnsupportedOperationException();
	}

	@Override
	@RequestMapping("/available")
	public List<LampHueDTO> findAllAvailable() {
		return lampService.findAllAvailable();
	}
	
	@RequestMapping("/countAvailable")
	public long countAvailable() {
		return lampService.findAllAvailable().size();
	}

	@Override
	@RequestMapping("/team/{teamId}")
	public TeamLampsDTO findAllOfATeam(@PathVariable long teamId) throws TeamDoesNotExistException {
		return lampService.findAllOfATeam(teamId);
	}
	
	@Override
	@RequestMapping("/team/{teamId}/nameOnly")
	public List<LampNameDTO> findAllLampNamesOfATeam(@PathVariable long teamId) throws TeamDoesNotExistException {
		return lampService.findAllLampNamesOfATeam(teamId);
	}

	@Override
	@RequestMapping(value = "/testScenario", method = RequestMethod.POST)
	public void testScenario(@RequestBody @Valid LampTestDTO lamp) {
		lampService.testScenario(lamp);
	}
	
	@Override
	@RequestMapping("/pulseOnce/{hueUniqueId}")
	public void pulseOnce(@PathVariable String hueUniqueId) {
		lampService.pulseOnce(hueUniqueId);
	}

	@Override
	@RequestMapping(value = "/turnOff", method = RequestMethod.POST)
	public void turnOff(@RequestBody @Valid LampTurnOffDTO lamp) {
		lampService.turnOff(lamp);
	}

}
