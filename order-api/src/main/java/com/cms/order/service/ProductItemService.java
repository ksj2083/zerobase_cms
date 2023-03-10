package com.cms.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cms.order.domain.model.Product;
import com.cms.order.domain.model.ProductItem;
import com.cms.order.domain.product.AddProductItemForm;
import com.cms.order.domain.repository.ProductItemRepository;
import com.cms.order.domain.repository.ProductRepository;
import com.cms.order.exception.CustomException;
import com.cms.order.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductItemService {
	private final ProductRepository productRepository;
	private final ProductItemRepository productItemRepository;

	@Transactional
	public Product addProductItem(Long sellerId, AddProductItemForm form) {
		Product product = productRepository.findBySellerIdAndId(sellerId, form.getProductId())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

		if (product.getProductItems().stream()
			.anyMatch(item -> item.getName().equals(form.getName()))) {
			throw new CustomException(ErrorCode.SAME_ITEM_NAME);
		}

		ProductItem productItem = ProductItem.of(sellerId, form);
		product.getProductItems().add(productItem);

		return product;
	}
}
