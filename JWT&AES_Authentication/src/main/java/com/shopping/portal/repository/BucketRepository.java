package com.shopping.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.portal.model.Bucket;

public interface BucketRepository extends JpaRepository<Bucket, Long>{

}
