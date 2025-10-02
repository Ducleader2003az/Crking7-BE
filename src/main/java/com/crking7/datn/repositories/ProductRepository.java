package com.crking7.datn.repositories;

import com.crking7.datn.models.Category;
import com.crking7.datn.models.Product;
import com.crking7.datn.models.Sale;
import com.crking7.datn.web.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByStatus(Pageable pageable, int status);

    Product findByIdAndStatus(long productId, int status);

    Product findByName(String name);


    Page<Product> findAllByProductCategoryAndStatus(Pageable pageable, Category category, int status);

    @Query("select max(p.id) from Product p")
    Long findNewestId();

    @Query("select p from Product p where p.name like %:keyword% and p.status = 1")
    Page<Product> searchAllByKeyword(@Param("keyword") String keyword,
                                     Pageable pageable);

//    @Query("select p from Product p " +
//            "join p.productCategory ca " +
//            "join p.colors c " +
//            "join c.sizes s " +
//            "where s.value like %:valueSize% " +
//            "and c.value like %:valueColor% " +
//            "and p.status = 1 " +
//            "and ca.id = :categoryId " +
//            "and p.price between :minPrice and :maxPrice")
//    Page<Product> searchAllBySizeColorPrice(@Param("valueSize") String valueSize,
//                                            @Param("valueColor") String valueColor,
//                                            @Param("minPrice") int minPrice,
//                                            @Param("maxPrice") int maxPrice,
//                                            @Param("categoryId") long categoryId,
//                                            Pageable pageable);

    @Query("select p from Product p " +
            "join p.colors c " +
            "join c.sizes s " + "where s.value like %:valueSize% and p.status = 1")
    Page<Product> searchAllByValueSize(@Param("valueSize") String valueSize, Pageable pageable);

    @Query("select p from Product p " +
            "join p.colors c " + "where c.value like %:valueColor% and p.status = 1")
    Page<Product> searchAllByValueColor(@Param("valueColor") String valueColor, Pageable pageable);

    @Query("select p from Product p " + "where p.status = 1 " + "and p.price between :minPrice and :maxPrice")
    Page<Product> searchAllByPrice(@Param("minPrice") int minPrice,
                                   @Param("maxPrice") int maxPrice,
                                   Pageable pageable);

    @Query("select p from Product p " +
            "join p.productCategory ca " +
            "join p.colors c " +
            "join c.sizes s " +
            "where p.status = 1 " +
            "and ca.id = :categoryId " +
            "and (:valueSize is null or s.value like %:valueSize%) " +
            "and (:valueColor is null or c.value like %:valueColor%) " +
            "and (:minPrice is null or p.price >= :minPrice) " +
            "and (:maxPrice is null or p.price <= :maxPrice)")
    Page<Product> searchProductInCategory(@Param("valueSize") String valueSize,
                                          @Param("valueColor") String valueColor,
                                          @Param("minPrice") Integer minPrice,
                                          @Param("maxPrice") Integer maxPrice,
                                          @Param("categoryId") long categoryId,
                                          Pageable pageable);

    @Query("select p from Product p where (:keyword is null or p.name like %:keyword% or p.description like %:keyword%)")
    Page<Product> getAllByKeyword(@Param("keyword") String keyword,
                                  Pageable pageable);

    List<Product> findBySaleId(Long id);

    Page<Product> findAllBySaleAndStatus(Sale sale, Pageable pageable, int status);

    @Query(value = "select * from product where product_category_id = :categoryId order by rand() limit :limit", nativeQuery = true)
    List<Product> findRelatedProducts(Long categoryId, int limit);

    @Query("select p from Product p " +
            "join p.productCategory ca " +
            "join p.colors c " +
            "join c.sizes s " +
            "group by p, s " +
            "order by s.sold desc")
    Page<Product> getBestSellerProducts(Pageable pageable);

    @Query("SELECT p " +
            "FROM Product p " +
            "JOIN p.colors c " +
            "JOIN c.sizes s " +
            "WHERE (:isActive = true AND s.total > 0) OR (:isActive = false AND s.total = 0)")
    Page<Product> getProductByQuantity(@Param("isActive") boolean isActive, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.sale IS NULL OR p.sale.id IS NULL) AND " +
            "(:keyword IS NULL OR p.name LIKE %:keyword% OR p.sku LIKE %:keyword%)")
    Page<Product> getProductNoSale(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.sale.id = :saleId) AND " +
            "(:keyword IS NULL OR p.name LIKE %:keyword% OR p.sku LIKE %:keyword%)")
    List<Product> findBySaleIdAndKeyword(@Param("keyword") String keyword, @Param("saleId") long saleId);

    @Query(value = "SELECT * FROM product WHERE status = 1 ORDER BY RAND() LIMIT 10", nativeQuery = true)
    List<Product> getRandomProducts();

    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.colors c " +
            "JOIN c.sizes s " +
            "WHERE p.status = 1 " +
            "AND (:categoryId IS NULL OR p.productCategory.id = :categoryId OR :categoryId = 0 OR p.productCategory.parentCategory.id = :categoryId OR " +
            "p.productCategory.parentCategory.parentCategory.id = :categoryId) " +
            "AND (:valueSize IS NULL OR s.value like %:valueSize% OR :valueSize = '') " +
            "AND (:valueColor IS NULL OR c.value like %:valueColor% OR :valueColor = '') " +
            "AND (:minPrice IS NULL OR p.salePrice >= :minPrice OR :minPrice = 0) " +
            "AND (:maxPrice IS NULL OR p.salePrice <= :maxPrice OR :maxPrice = 0) " +
            "AND (:saleId IS NULL OR p.sale.id = :saleId OR :saleId = 0)")
    Page<Product> getAllProducts(String valueSize,
                                 String valueColor,
                                 Integer minPrice,
                                 Integer maxPrice,
                                 Long categoryId,
                                 Long saleId,
                                 Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p " +
            "WHERE (:status IS NULL OR p.status = :status) " +
            "AND (:keyword IS NULL OR p.name LIKE %:keyword% OR p.sku LIKE %:keyword%) " +
            "AND (:categoryId IS NULL OR p.productCategory.id = :categoryId OR :categoryId = 0 OR p.productCategory.parentCategory.id = :categoryId OR " +
            "p.productCategory.parentCategory.parentCategory.id = :categoryId) " +
            "AND (:minPrice IS NULL OR p.salePrice >= :minPrice OR :minPrice = 0) " +
            "AND (:maxPrice IS NULL OR p.salePrice <= :maxPrice OR :maxPrice = 0)")
    Page<Product> getAllProductsAdmin(String keyword,
                                      Integer status,
                                      Integer minPrice,
                                      Integer maxPrice,
                                      Long categoryId,
                                      Pageable pageable);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.status = 1")
    Long totalProducts();

    @Query("SELECT p FROM Product p WHERE p.sale IS NOT NULL ")
    List<Product> findBySale();
}
