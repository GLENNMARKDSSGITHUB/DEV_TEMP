package com.dss.service;

import com.dss.Resources;
import com.dss.entity.actors.Actors;
import com.dss.entity.movie.DssMovie;
import com.dss.repository.actors.ActorsRepository;
import com.dss.repository.movie.DssMovieRepository;
import com.dss.util.exception.MovieNotExistingException;
import com.dss.util.utils.CommonStringUtility;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActorsServiceTest {

    @MockBean
    private ActorsRepository actorsRepository;

    @MockBean
    private DssMovieRepository dssMovieRepository;

    @Autowired
    private ActorsService actorsService;

    private final Resources resources = new Resources();

    @Test
    public void testAddActorSuccess() throws MovieNotExistingException {
        List<DssMovie> movieList = dssMovieRepository.findDssMovieByMovieId(resources.newActorsDto().getMovieId());
        Mockito.when(movieList).thenReturn(Collections.singletonList(resources.dssMovie()));
        Assert.assertEquals(Boolean.TRUE, actorsService.addActor(resources.newActorsDto()).isSuccess());
    }

    @Test
    public void testActorsExist() throws MovieNotExistingException {
        List<DssMovie> movieList = dssMovieRepository.findDssMovieByMovieId(resources.actorsDto().getMovieId());
        Mockito.when(movieList).thenReturn(Collections.singletonList(resources.dssMovie()));
        Assert.assertEquals(Boolean.FALSE, actorsService.addActor(resources.actorsDto()).isSuccess());
    }

    @Test(expected = MovieNotExistingException.class)
    public void testMovieNotExist_Add() throws MovieNotExistingException {
        List<DssMovie> movieList = dssMovieRepository.findDssMovieByMovieId(resources.actorWithDtoNewMovieId().getMovieId());
        Mockito.when(movieList).thenReturn(Collections.singletonList(resources.dssMovie()));

        Mockito.when(actorsService.addActor(resources.actorsDto()))
                .thenThrow(new MovieNotExistingException(CommonStringUtility.ERR_MSG_MOVIE_NOT_EXIST));
    }

    @Test
    public void testDisplayActorsSuccess(){
        List<Actors> actorsList = actorsRepository.findAll();
        Mockito.when(actorsList).thenReturn(Collections.singletonList(resources.actors()));
        Assert.assertEquals(1, actorsService.displayActors().getObjList().size());
    }

    @Test
    public void testDisplayActorsFailed(){
        List<Actors> actorsList = new ArrayList<>();
        Mockito.when(actorsRepository.findAll()).thenReturn(actorsList);
        Assert.assertEquals(Boolean.FALSE, actorsService.displayActors().isSuccess());
    }

    @Test
    public void testSearchActorByActorNameSuccess(){
        List<Actors> actorsList = actorsRepository.findActorsByActorName("Robert", "Downey Jr.");
        Mockito.when(actorsList).thenReturn(Collections.singletonList(resources.actors()));
        Assert.assertEquals(Boolean.TRUE, actorsService.searchActorByActorName("Robert", "Downey Jr.").isSuccess());
    }

    @Test
    public void testSearchActorFailed(){
        List<Actors> actorsList = actorsRepository.findActorsByActorName("Robert", "Downey Jr.");
        Mockito.when(actorsList).thenReturn(Collections.singletonList(resources.actors()));
        Assert.assertEquals(Boolean.FALSE, actorsService.searchActorByActorName("Glenn Mark", "Anduiza").isSuccess());
    }

    @Test
    public void testUpdateActorSuccess() throws MovieNotExistingException {
        List<DssMovie> movieList = dssMovieRepository.findDssMovieByMovieId(resources.actorsDto().getMovieId());
        Mockito.when(movieList).thenReturn(Collections.singletonList(resources.dssMovie()));
        resources.actorsDto().setAge(20);
        Assert.assertEquals(Boolean.TRUE, actorsService.updateActor(resources.actorsDto()).isSuccess());
    }

    @Test(expected = MovieNotExistingException.class)
    public void testMovieNotExist_Update() throws MovieNotExistingException {
        List<DssMovie> movieList = dssMovieRepository.findDssMovieByMovieId(resources.actorWithDtoNewMovieId().getMovieId());
        Mockito.when(movieList).thenReturn(Collections.singletonList(resources.dssMovie()));

        Mockito.when(actorsService.updateActor(resources.actorsDto()))
                .thenThrow(new MovieNotExistingException(CommonStringUtility.ERR_MSG_MOVIE_NOT_EXIST));
    }

    @Test
    public void testActorsNotExist() throws MovieNotExistingException {
        List<DssMovie> movieList = dssMovieRepository.findDssMovieByMovieId(resources.actorsDto().getMovieId());
        Mockito.when(movieList).thenReturn(Collections.singletonList(resources.dssMovie()));
        Assert.assertEquals(Boolean.FALSE, actorsService.updateActor(resources.newActorsDto()).isSuccess());
    }

    @Test
    public void testDeleteActorSuccess(){
        List<Actors> actorsList = actorsRepository.findActorsByActorName("Robert", "Downey Jr.");
        Mockito.when(actorsList).thenReturn(Collections.singletonList(resources.actors()));
        Assert.assertEquals(Boolean.TRUE, actorsService.deleteActor("Robert", "Downey Jr.").isSuccess());
    }

    @Test
    public void testDeleteActorFailed(){
        List<Actors> actorsList = actorsRepository.findActorsByActorName("Robert", "Downey Jr.");
        Mockito.when(actorsList).thenReturn(Collections.singletonList(resources.actors()));
        Assert.assertEquals(Boolean.FALSE, actorsService.deleteActor("Glenn Mark", "Anduiza").isSuccess());
    }
}
