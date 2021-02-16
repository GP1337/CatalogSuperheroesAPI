package com.example.сatalogSuperheroesAPI.controller;

import com.example.сatalogSuperheroesAPI.model.Character;
import com.example.сatalogSuperheroesAPI.model.Comic;
import com.example.сatalogSuperheroesAPI.model.ResponseMessage;
import com.example.сatalogSuperheroesAPI.service.CharacterService;
import com.example.сatalogSuperheroesAPI.service.ComicCharacterRelationService;
import com.example.сatalogSuperheroesAPI.service.ComicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @Autowired
    private ComicService comicService;

    @Autowired
    private ComicCharacterRelationService comicCharacterRelationService;

    @GetMapping
    public ResponseEntity<List<Character>> getCharacterList(@RequestParam(required = false) Map<String, String> params){

        return new ResponseEntity(new ResponseMessage(characterService.getCharacterList(params)), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity getCharacterById(@PathVariable BigInteger id){

        Character dbCharacter = characterService.getCharacterById(id);

        if (dbCharacter == null){
            return new ResponseEntity(new ResponseMessage(ResponseMessage.getElementNotFoundMessage(dbCharacter.getClass(), id)), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(new ResponseMessage(dbCharacter), HttpStatus.OK);

    }

    @GetMapping("/{id}/comics")
    public ResponseEntity getCharactersComix(@PathVariable BigInteger id){

        Character dbCharacter = characterService.getCharacterById(id);

        if (dbCharacter == null){
            return new ResponseEntity(new ResponseMessage(ResponseMessage.getElementNotFoundMessage(dbCharacter.getClass(), id)), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(new ResponseMessage(dbCharacter.getComics()), HttpStatus.OK);

    }

    @PostMapping("/{characterId}/comics/{comicId}")
    public ResponseEntity addCharactersComics(@PathVariable BigInteger characterId, @PathVariable BigInteger comicId){

        Character character = characterService.getCharacterById(characterId);
        Comic comic = comicService.getComicById(comicId);

        List<String> errors = new ArrayList();

        if (character == null){
            errors.add(ResponseMessage.getElementNotFoundMessage(character.getClass(), characterId));
        }

        if (comic == null){
            errors.add(ResponseMessage.getElementNotFoundMessage(comic.getClass(), comicId));
        }

        if (errors.size() > 0){
            return new ResponseEntity(new ResponseMessage(errors.toString()), HttpStatus.NOT_FOUND);
        }

        comicCharacterRelationService.saveRelation(character, comic);

        return new ResponseEntity(ResponseMessage.ResponsePostPutOk(), HttpStatus.OK);

    }

    @DeleteMapping("/{characterId}/comics/{comicId}")
    public ResponseEntity removeCharactersComics(@PathVariable BigInteger characterId, @PathVariable BigInteger comicId){

        Character character = characterService.getCharacterById(characterId);
        Comic comic = comicService.getComicById(comicId);

        List<String> errors = new ArrayList();

        if (character == null){
            errors.add(ResponseMessage.getElementNotFoundMessage(character.getClass(), characterId));
        }

        if (comic == null){
            errors.add(ResponseMessage.getElementNotFoundMessage(comic.getClass(), characterId));
        }

        if (errors.size() > 0){
            return new ResponseEntity(new ResponseMessage(errors.toString()), HttpStatus.NOT_FOUND);
        }

        comicCharacterRelationService.saveRelation(character, comic);

        return new ResponseEntity(ResponseMessage.ResponsePostPutOk(), HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity postCharacter(@RequestBody Character character, @PathVariable(required = false /*post can be used either to update or to create character*/) BigInteger id){

        if (character.getId() != null) {
            return new ResponseEntity(new ResponseMessage(ResponseMessage.getIdUnacceptableMessage()), HttpStatus.BAD_REQUEST);
        }

        if (id != null) {

            if (characterService.getCharacterById(id) == null){
                return new ResponseEntity(new ResponseMessage(ResponseMessage.getElementNotFoundMessage(character.getClass(), id)), HttpStatus.NOT_FOUND);
            }

            characterService.changeCharacterById(id, character);

            return new ResponseEntity(ResponseMessage.ResponsePostPutOk(), HttpStatus.OK);

        }

        characterService.addCharacter(character);

        return new ResponseEntity(ResponseMessage.ResponsePostPutOk(), HttpStatus.OK);

    }

    @PutMapping
    public ResponseEntity putCharacter(@RequestBody Character character, @PathVariable BigInteger id){

        if (character.getId() != null){
            return new ResponseEntity(new ResponseMessage(ResponseMessage.getIdUnacceptableMessage()), HttpStatus.BAD_REQUEST);
        }

        characterService.changeCharacterById(id, character);

        return new ResponseEntity(ResponseMessage.ResponsePostPutOk(), HttpStatus.OK);

    }

}
