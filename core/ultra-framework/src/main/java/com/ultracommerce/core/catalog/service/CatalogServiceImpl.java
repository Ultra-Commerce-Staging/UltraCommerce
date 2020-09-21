/*
 * #%L
 * UltraCommerce Framework
 * %%
 * Copyright (C) 2009 - 2016 Ultra Commerce
 * %%
 * Licensed under the Ultra Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.ultracommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Ultra in which case
 * the Ultra End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.ultracommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Ultra Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */
package com.ultracommerce.core.catalog.service;

import org.apache.commons.collections.MapUtils;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.core.catalog.dao.CategoryDao;
import com.ultracommerce.core.catalog.dao.ProductDao;
import com.ultracommerce.core.catalog.dao.ProductOptionDao;
import com.ultracommerce.core.catalog.dao.SkuDao;
import com.ultracommerce.core.catalog.domain.Category;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.domain.ProductBundle;
import com.ultracommerce.core.catalog.domain.ProductBundleComparator;
import com.ultracommerce.core.catalog.domain.ProductOption;
import com.ultracommerce.core.catalog.domain.ProductOptionValue;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.domain.SkuFee;
import com.ultracommerce.core.catalog.domain.dto.AssignedProductOptionDTO;
import com.ultracommerce.core.catalog.service.type.ProductType;
import com.ultracommerce.core.search.domain.SearchCriteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Service("ucCatalogService")
public class CatalogServiceImpl implements CatalogService {

    @Resource(name="ucCategoryDao")
    protected CategoryDao categoryDao;

    @Resource(name="ucProductDao")
    protected ProductDao productDao;

    @Resource(name="ucSkuDao")
    protected SkuDao skuDao;
    
    @Resource(name="ucProductOptionDao")
    protected ProductOptionDao productOptionDao;

    @Resource(name = "ucCatalogServiceExtensionManager")
    protected CatalogServiceExtensionManager extensionManager;

    @Override
    public Product findProductById(Long productId) {
        return productDao.readProductById(productId);
    }
    
    @Override
    public Product findProductByExternalId(String externalId) {
        return productDao.readProductByExternalId(externalId);
    }

    @Override
    public List<Product> findProductsByName(String searchName) {
        return productDao.readProductsByName(searchName);
    }

    @Override
    public List<Product> findProductsByName(String searchName, int limit, int offset) {
        return productDao.readProductsByName(searchName, limit, offset);
    }

    @Override
    public List<Product> findActiveProductsByCategory(Category category) {
        return productDao.readActiveProductsByCategory(category.getId());
    }

    @Override
    public List<Product> findFilteredActiveProductsByCategory(Category category, SearchCriteria searchCriteria) {
        return productDao.readFilteredActiveProductsByCategory(category.getId(), searchCriteria);
    }

    @Override
    public List<Product> findFilteredActiveProductsByQuery(String query, SearchCriteria searchCriteria) {
        return productDao.readFilteredActiveProductsByQuery(query, searchCriteria);
    }

    @Override
    public List<Product> findActiveProductsByCategory(Category category, int limit, int offset) {
        return productDao.readActiveProductsByCategory(category.getId(), limit, offset);
    }

    @Override
    @Deprecated
    public List<Product> findActiveProductsByCategory(Category category, Date currentDate) {
        return productDao.readActiveProductsByCategory(category.getId(), currentDate);
    }
    
    @Override
    @Deprecated
    public List<Product> findFilteredActiveProductsByCategory(Category category, Date currentDate, SearchCriteria searchCriteria) {
        return productDao.readFilteredActiveProductsByCategory(category.getId(), currentDate, searchCriteria);
    }
    
    @Override
    @Deprecated
    public List<Product> findFilteredActiveProductsByQuery(String query, Date currentDate, SearchCriteria searchCriteria) {
        return productDao.readFilteredActiveProductsByQuery(query, currentDate, searchCriteria);
    }

    @Override
    @Deprecated
    public List<Product> findActiveProductsByCategory(Category category, Date currentDate, int limit, int offset) {
        return productDao.readActiveProductsByCategory(category.getId(), currentDate, limit, offset);
    }

    @Override
    public List<ProductBundle> findAutomaticProductBundles() {
        List<ProductBundle> bundles =  productDao.readAutomaticProductBundles();
        Collections.sort(bundles, new ProductBundleComparator());
        return bundles;
    }

    @Override
    @Transactional("ucTransactionManager")
    public Product saveProduct(Product product) {
        return productDao.save(product);
    }

    @Override
    public Category findCategoryById(Long categoryId) {
        return categoryDao.readCategoryById(categoryId);
    }

    @Override
    public Category findCategoryByExternalId(String externalId) {
        return categoryDao.readCategoryByExternalId(externalId);
    }

    @Override
    @Deprecated
    public Category findCategoryByName(String categoryName) {
        return categoryDao.readCategoryByName(categoryName);
    }

    @Override
    public List<Category> findCategoriesByName(String categoryName) {
        return categoryDao.readCategoriesByName(categoryName);
    }

    @Override
    public List<Category> findCategoriesByName(String categoryName, int limit, int offset) {
        return categoryDao.readCategoriesByName(categoryName, limit, offset);
    }

    @Override
    @Transactional("ucTransactionManager")
    public Category saveCategory(Category category) {
        return categoryDao.save(category);
    }
    
    @Override
    @Transactional("ucTransactionManager")
    public void removeCategory(Category category){
        categoryDao.delete(category);
    }
    
    @Override
    @Transactional("ucTransactionManager")
    public void removeSku(Sku sku) {
        skuDao.delete(sku);
    }

    @Override
    @Transactional("ucTransactionManager")
    public void removeProduct(Product product) {
        productDao.delete(product);
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryDao.readAllCategories();
    }

    @Override
    public List<Category> findAllCategories(int limit, int offset) {
        return categoryDao.readAllCategories(limit, offset);
    }

    @Override
    public Long findTotalCategoryCount() {
        return categoryDao.readTotalCategoryCount();
    }

    @Override
    public List<Category> findAllSubCategories(Category category) {
        return categoryDao.readAllSubCategories(category);
    }

    @Override
    public List<Category> findAllSubCategories(Category category, int limit, int offset) {
        return categoryDao.readAllSubCategories(category, limit, offset);
    }

    @Override
    public List<Category> findActiveSubCategoriesByCategory(Category category) {
        return categoryDao.readActiveSubCategoriesByCategory(category);
    }

    @Override
    public List<Category> findActiveSubCategoriesByCategory(Category category, int limit, int offset) {
        return categoryDao.readActiveSubCategoriesByCategory(category, limit, offset);
    }

    @Override
    public List<Product> findAllProducts() {
        return categoryDao.readAllProducts();
    }

    @Override
    public List<Product> findAllProducts(int limit, int offset) {
        return categoryDao.readAllProducts(limit, offset);
    }

    @Override
    public List<Sku> findAllSkus() {
        return skuDao.readAllSkus();
    }

    @Override
    public List<Sku> findAllSkus(int offset, int limit) {
        return skuDao.readAllSkus(offset, limit);
    }

    @Override
    public Sku findSkuById(Long skuId) {
        return skuDao.readSkuById(skuId);
    }

    @Override
    public Sku findSkuByExternalId(String externalId) {
        return skuDao.readSkuByExternalId(externalId);
    }

    @Override
    public Sku findSkuByUpc(String upc) {
        return skuDao.readSkuByUpc(upc);
    }

    @Override
    @Transactional("ucTransactionManager")
    public Sku saveSku(Sku sku) {
        return skuDao.save(sku);
    }
    
    @Override
    @Transactional("ucTransactionManager")
    public SkuFee saveSkuFee(SkuFee fee) {
        return skuDao.saveSkuFee(fee);
    }
    
    @Override
    public List<Sku> findSkusByIds(List<Long> ids) {
        return skuDao.readSkusByIds(ids);
    }

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public void setSkuDao(SkuDao skuDao) {
        this.skuDao = skuDao;
    }

    @Override
    public List<Product> findProductsForCategory(Category category) {
        return productDao.readProductsByCategory(category.getId());
    }

    @Override
    public List<Product> findProductsForCategory(Category category, int limit, int offset) {
        return productDao.readProductsByCategory(category.getId(), limit, offset);
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    @Deprecated
    public Map<String, List<Long>> getChildCategoryURLMapByCategoryId(Long categoryId) {
        Category category = findCategoryById(categoryId);
        if (category != null) {
            return category.getChildCategoryURLMap();
        }
        return null;
    }
    
    @Override
    public Category createCategory() {
        return categoryDao.create();
    }
    
    @Override
    public Sku createSku() {
        return skuDao.create();
    }
    
    @Override
    public Product createProduct(ProductType productType) {
        return productDao.create(productType);
    }

    @Override
    public List<ProductOption> readAllProductOptions() {
        return productOptionDao.readAllProductOptions();
    }
    
    @Override
    @Transactional("ucTransactionManager")
    public ProductOption saveProductOption(ProductOption option) {
        return productOptionDao.saveProductOption(option);
    }
    
    @Override
    public ProductOption findProductOptionById(Long productOptionId) {
        return productOptionDao.readProductOptionById(productOptionId);
    }

    @Override
    public ProductOptionValue findProductOptionValueById(Long productOptionValueId) {
        return productOptionDao.readProductOptionValueById(productOptionValueId);
    }

    protected CatalogContextDTO createCatalogContextDTO() {
        UltraRequestContext ctx = UltraRequestContext.getUltraRequestContext();
        CatalogContextDTO context = new CatalogContextDTO();

        Map<String, Object> ruleMap = (Map<String, Object>) ctx.getRequestAttribute("ucRuleMap");

        if (MapUtils.isNotEmpty(ruleMap)) {
            context.setAttributes(ruleMap);
        }

        return context;
    }

    @Override
    public Category findCategoryByURI(String uri) {
        if (extensionManager != null) {
            ExtensionResultHolder holder = new ExtensionResultHolder();
            ExtensionResultStatusType result = extensionManager.getProxy().findCategoryByURI(createCatalogContextDTO(), uri, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return (Category) holder.getResult();
            }
        }
        return findOriginalCategoryByURI(uri);
    }

    @Override
    public Category findOriginalCategoryByURI(String uri) {
        return categoryDao.findCategoryByURI(uri);
    }

    @Override
    public Product findProductByURI(String uri) {
        if (extensionManager != null) {
            ExtensionResultHolder holder = new ExtensionResultHolder();
            ExtensionResultStatusType result = extensionManager.getProxy().findProductByURI(createCatalogContextDTO(), uri, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return (Product) holder.getResult();
            }
        }

        return findOriginalProductByURI(uri);
    }

    @Override
    public Product findOriginalProductByURI(String uri) {
        List<Product> products = productDao.findProductByURI(uri);
        if (products == null || products.size() == 0) {
            return null;
        } else if (products.size() == 1) {
            return products.get(0);
        } else {
            // First check for a direct hit on the url
            for(Product product : products) {
                if (uri.equals(product.getUrl())) {
                    return product;
                }
            }

            for(Product product : products) {
                // Next check for a direct hit on the generated URL.
                if (uri.equals(product.getGeneratedUrl())) {
                    return product;
                }
            }

            // Otherwise, return the first product
            return products.get(0);
        }
    }
    
    @Override
    public Sku findSkuByURI(String uri) {
        if (extensionManager != null) {
            ExtensionResultHolder holder = new ExtensionResultHolder();
            ExtensionResultStatusType result = extensionManager.getProxy().findSkuByURI(createCatalogContextDTO(), uri, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                return (Sku) holder.getResult();
            }
        }
        List<Sku> skus = skuDao.findSkuByURI(uri);
        if (skus == null || skus.size() == 0) {
            return null;
        } else if (skus.size() == 1) {
            return skus.get(0);
        } else {
            // First check for a direct hit on the url
            for(Sku sku : skus) {
                if (uri.equals(sku.getProduct().getUrl() + sku.getUrlKey())) {
                    return sku;
                }
            }
            
            // Otherwise, return the first product
            return skus.get(0);
        }
    }

    @Override
    public List<AssignedProductOptionDTO> findAssignedProductOptionsByProductId(Long productId) {
        return productOptionDao.findAssignedProductOptionsByProductId(productId);
    }

    @Override
    public List<AssignedProductOptionDTO> findAssignedProductOptionsByProduct(Product product) {
        return productOptionDao.findAssignedProductOptionsByProduct(product);
    }

    @Override
    public Long countProductsUsingProductOptionById(Long productOptionId) {
        return productOptionDao.countProductsUsingProductOptionById(productOptionId);
    }

    @Override
    public List<Long> findProductIdsUsingProductOptionById(Long productId, int start, int pageSize) {
        return productOptionDao.findProductIdsUsingProductOptionById(productId, start, pageSize);
    }
}
