package com.ask.springjpacore.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

public abstract class GenericService<T, ID extends Serializable, R extends JpaRepository<T, ID>> {

  private R repository;

  @Autowired
  public void setRepository(ApplicationContext applicationContext) {
    this.repository = applicationContext.getBean(getRepositoryClass());
  }

  @SuppressWarnings("unchecked")
  private Class<R> getRepositoryClass() {
    List<TypeInformation<?>> arguments = ClassTypeInformation.from(this.getClass())
        .getRequiredSuperTypeInformation(GenericService.class)
        .getTypeArguments();

    return (Class<R>) arguments.get(2).getType();
  }

  public List<T> findAll() {
    return repository.findAll();
  }

  public Optional<T> findById(ID id) {
    return repository.findById(id);
  }

  public boolean existsById(ID id) {
    return repository.existsById(id);
  }

  public T save(T entity) {
    return repository.save(entity);
  }

  public <S extends T> S saveAndFlush(S entity) {
    return repository.saveAndFlush(entity);
  }

  public void deleteById(ID id) {
    repository.deleteById(id);
  }

  public void delete(T entity) {
    repository.delete(entity);
  }

}
