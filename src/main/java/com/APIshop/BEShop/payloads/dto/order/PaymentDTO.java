package com.APIshop.BEShop.payloads.dto.order;

import com.APIshop.BEShop.enums.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    private PaymentMethod paymentMethod;

    private String paymentCode;
}
