package com.dss.service;

import com.dss.Resources;
import com.dss.entity.movie.DssMovie;
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
public class DssMovieServiceTest {

    @Autowired
    private DssMovieService dssMovieService;

    @MockBean
    private DssMovieRepository dssMovieRepository;

    private final Resources resources = new Resources();

    @Test
    public void testAddMovieSuccess(){
        List<DssMovie> movieList =  dssMovieRepository.findDssMovieByMovieId("DSSC1BD5CB");
        Mockito.when(movieList).thenReturn(Collections.singletonList(resources.dssMovie()));
        Assert.assertEquals(Boolean.TRUE, dssMovieService.addDssMovie(resources.dssMovieDto()).isSuccess());
    }

    @Test
    public void testAddMovieFailed(){
        Mockito.when(dssMovieRepository.findDssMovieByMovieTitle(resources.dssMovie().getMovieTitle()))
                .thenReturn(Collections.singletonList(resources.dssMovie()));
        Assert.assertEquals(Boolean.FALSE, dssMovieService.addDssMovie(resources.dssMovieDto()).isSuccess());
    }

    @Test
    public void testDisplayMovieSuccess(){
        Mockito.when(dssMovieRepository.findAll()).thenReturn(Collections.singletonList(resources.dssMovie()));
        Assert.assertEquals(1, dssMovieService.displayDssMovies().getObjList().size());
    }

    @Test
    public void testDisplayMovieFailed(){
        List<DssMovie> movieList = new ArrayList<>();
        Mockito.when(dssMovieRepository.findAll()).thenReturn(movieList);
        Assert.assertEquals(Boolean.FALSE, dssMovieService.displayDssMovies().isSuccess());
    }

    @Test
    public void testSearchMovieSuccess() throws MovieNotExistingException {
        Mockito.when(dssMovieRepository.findDssMovieByMovieTitle(resources.dssMovie().getMovieTitle()))
                .thenReturn(Collections.singletonList(resources.dssMovie()));
        Assert.assertEquals(Boolean.TRUE, dssMovieService.searchDssMovieByMovieTitle(resources.dssMovie().getMovieTitle()).isSuccess());
    }

    @Test(expected = MovieNotExistingException.class)
    public void testSearchMovieFailed() throws MovieNotExistingException {
        List<DssMovie> movieList = dssMovieRepository.findDssMovieByMovieTitle("Sample Movie Title");
        Mockito.when(movieList).thenReturn(Collections.singletonList(resources.dssMovie()));

        Mockito.when(dssMovieService.searchDssMovieByMovieTitle(resources.dssMovie().getMovieTitle()))
                .thenThrow(new MovieNotExistingException(CommonStringUtility.ERR_MSG_MOVIE_NOT_EXIST));
    }

    @Test
    public void testUpdateMovieSuccess() throws MovieNotExistingException {
        List<DssMovie> movieList = dssMovieRepository.findDssMovieByMovieId(resources.dssMovieDto().getMovieId());
        Mockito.when(movieList).thenReturn(Collections.singletonList(resources.dssMovie()));

        resources.dssMovieDto().setDirectedBy("Glenn Mark Anduiza");
        Assert.assertEquals(Boolean.TRUE, dssMovieService.updateDssMovie(resources.dssMovieDto()).isSuccess());
    }

    @Test(expected = MovieNotExistingException.class)
    public void testUpdateMovieFailed() throws MovieNotExistingException {
        List<DssMovie> movieList = dssMovieRepository.findDssMovieByMovieId("UC12345678");
        Mockito.when(movieList).thenReturn(Collections.singletonList(resources.dssMovie()));

        Mockito.when(dssMovieService.updateDssMovie(resources.dssMovieDto()))
                .thenThrow(new MovieNotExistingException(CommonStringUtility.ERR_MSG_MOVIE_NOT_EXIST));
    }

    @Test
    public void testDeleteMovieSuccess() throws MovieNotExistingException {
        List<DssMovie> movieList = dssMovieRepository.findDssMovieByMovieTitle(resources.dssMovie().getMovieTitle());
        Mockito.when(movieList).thenReturn(Collections.singletonList(resources.dssMovie()));
        Assert.assertEquals(Boolean.TRUE, dssMovieService.deleteDssMovie(resources.dssMovie().getMovieTitle()).isSuccess());
    }

    @Test(expected = MovieNotExistingException.class)
    public void testDeleteMovieFailed() throws MovieNotExistingException {
        List<DssMovie> movieList = dssMovieRepository.findDssMovieByMovieTitle("Sample Movie Title");
        Mockito.when(movieList).thenReturn(Collections.singletonList(resources.dssMovie()));

        Mockito.when(dssMovieService.deleteDssMovie(resources.dssMovie().getMovieTitle()))
                .thenThrow(new MovieNotExistingException(CommonStringUtility.ERR_MSG_MOVIE_NOT_EXIST));
    }
}
