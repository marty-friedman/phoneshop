package com.es.phoneshop.web.controller.pages;

import com.es.phoneshop.core.cart.model.CartItem;
import com.es.phoneshop.core.cart.service.CartService;
import com.es.phoneshop.core.cart.throwable.NoStockFoundException;
import com.es.phoneshop.core.cart.throwable.NoSuchPhoneException;
import com.es.phoneshop.core.cart.throwable.TooBigQuantityException;
import com.es.phoneshop.core.phone.model.Phone;
import com.es.phoneshop.web.controller.form.UpdateCartForm;
import com.es.phoneshop.web.controller.throwable.InternalException;
import com.es.phoneshop.web.controller.util.UpdateCartItem;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
public class CartPageController {
    @Resource
    private CartService cartService;

    @ModelAttribute("updateCartForm")
    private UpdateCartForm makeUpdateCartForm() {
        UpdateCartForm form = new UpdateCartForm();
        form.setUpdateCartItems(
                cartService.getCart().getCartItems().stream()
                .map(UpdateCartItem::fromCartItem)
                .collect(Collectors.toList())
        );
        return form;
    }

    @ModelAttribute("phones")
    private List<Phone> getPhones() {
        return cartService.getCart().getCartItems().stream()
                .map(CartItem::getPhone)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showCart() {
        return "cart";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String updateCart(@ModelAttribute("updateCartForm") @Valid UpdateCartForm form, BindingResult result) {
        if (result.hasFieldErrors())
            return "cart";
        List<UpdateCartItem> updateCartItems = form.getUpdateCartItems();
        try {
            Map<Long, Long> updateMap = updateCartItems.stream()
                    .collect(Collectors.toMap(UpdateCartItem::getPhoneId, UpdateCartItem::getQuantity));
            cartService.update(updateMap);
        } catch (TooBigQuantityException e) {
            handleTooBigQuantities(result, e.getPhoneIds(), updateCartItems);
        } catch (NoSuchPhoneException e) {
            return "redirect:/cart";
        } catch (NoStockFoundException e) {
            throw new InternalException();
        }
        if (result.hasFieldErrors())
            return "cart";
        return "redirect:/cart";
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String deleteFromCart(Long phoneId) {
        try {
            cartService.remove(phoneId);
        } catch (NoSuchPhoneException e) {
            throw new InternalException();
        }
        return "redirect:/cart";
    }

    private void handleTooBigQuantities(BindingResult result, Set<Long> phoneIds, List<UpdateCartItem> records) {
        for (int i = 0; i < records.size(); i++) {
            Long phoneId = records.get(i).getPhoneId();
            if (phoneIds.contains(phoneId))
                result.rejectValue("updateCartItems[" + i + "].quantity", "error.tooBig.quantity");
        }
    }
}
