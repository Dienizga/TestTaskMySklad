package com.example.customwarehousetask.api.controller;

import com.example.customwarehousetask.api.converter.ProductToResponseConverter;
import com.example.customwarehousetask.api.json.ProductRequest;
import com.example.customwarehousetask.api.json.ProductResponse;
import com.example.customwarehousetask.service.ProductService;
import com.example.customwarehousetask.service.DTO.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {
    private final ProductService service;
    private final ProductToResponseConverter converter;

    @GetMapping("/products")
    public @ResponseBody ResponseEntity<List<ProductResponse>> getList() {
        List<ProductDTO> products = service.getAll();
        return ok(products.stream().map(converter::convert).collect(Collectors.toList()));
    }

    @PostMapping("/create/product")
    public @ResponseBody ResponseEntity<ProductResponse> create(@Validated @RequestBody ProductRequest request) {
        ProductDTO productDTO = service.create(
                request.getArticle(),
                request.getName(),
                request.getLastPurchase(),
                request.getLastSale(),
                request.getWarehouse()
        );
        if (productDTO == null) {
            return status(HttpStatus.valueOf("such a product already exists")).build();
        }
        return ok(converter.convert(productDTO));
    }

    @PatchMapping("/edit/product")
    public @ResponseBody ResponseEntity<ProductResponse> edit(@Validated @RequestBody ProductRequest request) {
        ProductDTO productDTO = service.edit(
                request.getArticle(),
                request.getName(),
                request.getLastPurchase(),
                request.getLastSale(),
                request.getWarehouse()
        );
        if (productDTO == null) {
            return status(HttpStatus.valueOf("Not found")).build();
        }
        return ok(converter.convert(productDTO));
    }

    @DeleteMapping("/delete/product")
    private @ResponseBody ResponseEntity<String> delete(@Validated @RequestBody ProductRequest request) {
        service.delete(request.getArticle());
        return ok(request.getArticle() + " deleted");
    }
}
