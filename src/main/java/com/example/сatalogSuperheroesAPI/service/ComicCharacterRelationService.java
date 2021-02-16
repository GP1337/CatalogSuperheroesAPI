package com.example.сatalogSuperheroesAPI.service;

import com.example.сatalogSuperheroesAPI.model.Character;
import com.example.сatalogSuperheroesAPI.model.Comic;
import com.example.сatalogSuperheroesAPI.repository.CharacterRepository;
import com.example.сatalogSuperheroesAPI.repository.ComicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComicCharacterRelationService {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private ComicRepository comicRepository;

    @Transactional
    public void saveRelation(Character character, Comic comic){

        if (!character.getComics().contains(comic)){

            character.getComics().add(comic);

            characterRepository.save(character);

        }

        if (!comic.getCharacters().contains(character)){

            comic.getCharacters().add(character);

            comicRepository.save(comic);

        }

    }

    @Transactional
    public void removeRelation(Character character, Comic comic){

        if (character.getComics().contains(comic)){

            character.getComics().remove(comic);

            characterRepository.save(character);

        }

        if (comic.getCharacters().contains(character)){

            comic.getCharacters().remove(character);

            comicRepository.save(comic);

        }

    }

}
