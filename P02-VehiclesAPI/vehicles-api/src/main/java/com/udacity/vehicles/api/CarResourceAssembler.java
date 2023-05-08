package com.udacity.vehicles.api;

import com.udacity.vehicles.domain.car.Car;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Maps the CarController to the Car class using HATEOAS
 */
@Component
public class CarResourceAssembler implements RepresentationModelAssembler<Car, EntityModel<Car>> {

//    @Override
//    public Resource<Car> toResource(Car car) {
//        return new Resource<>(car,
//                linkTo(methodOn(CarController.class).get(car.getId())).withSelfRel(),
//                linkTo(methodOn(CarController.class).list()).withRel("cars"));
//
//    }

    @Override
    public EntityModel<Car> toModel(Car entity) {
        List<Link> links = new ArrayList<Link>();
        links.add(linkTo(methodOn(CarController.class).get(entity.getId())).withSelfRel());
        links.add(linkTo(methodOn(CarController.class).list()).withRel("cars"));
        return EntityModel.of(entity, links);
    }


    @Override
    public CollectionModel<EntityModel<Car>> toCollectionModel(Iterable<? extends Car> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
