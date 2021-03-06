package com.es.phoneshop.core.util;

public interface SQLQueries {
    String GET_PHONE = "SELECT * FROM phones WHERE id = ?";

    String GET_STOCK = "SELECT * FROM stocks WHERE phoneId = ?";

    String TEST_STOCK = "SELECT COUNT(*) FROM stocks WHERE phoneId = ?";

    String GET_ORDER = "SELECT * FROM orders WHERE id = ?";

    String GET_ORDERS = "SELECT * FROM orders";

    String TEST_ORDER = "SELECT COUNT(*) FROM orders WHERE id = ?";

    String INSERT_ORDER = "INSERT INTO orders (id, subtotal, deliveryPrice, totalPrice, firstName, lastName, deliveryAddress, contactPhoneNo, additionalInformation, status)" +
            "VALUES (:id, :subtotal, :deliveryPrice, :totalPrice, :firstName, :lastName, :deliveryAddress, :contactPhoneNo, :additionalInformation, :status)";

    String INSERT_ORDER_ITEMS = "INSERT INTO orderItems (orderId, phoneId, quantity) VALUES (:orderId, :phoneId, :quantity)";

    String GET_ORDER_ITEMS = "SELECT * FROM orderItems WHERE orderId = ?";

    String UPDATE_ORDER_STATUS = "UPDATE orders SET status = ? WHERE id = ?";

    String UPDATE_STOCK = "UPDATE stocks SET stock = ?, reserved = ? WHERE phoneId = ?";

    String GET_COLOR_IDS_FOR_PHONE = "SELECT colorId FROM phone2color WHERE phoneId = ?";

    String GET_COLORS_BY_IDS = "SELECT * FROM colors WHERE id IN (:ids)";

    String INSERT_PHONE = "" +
            "INSERT INTO phones (brand, model, price, displaySizeInches, weightGr, lengthMm, widthMm, " +
                "heightMm, announced, deviceType, os, displayResolution, pixelDensity, displayTechnology, " +
                "backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, " +
                "talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) " +
            "VALUES (:brand, :model, :price, :displaySizeInches, :weightGr, :lengthMm, :widthMm, " +
                ":heightMm, :announced, :deviceType, :os, :displayResolution, :pixelDensity, :displayTechnology, " +
                ":backCameraMegapixels, :frontCameraMegapixels, :ramGb, :internalStorageGb, :batteryCapacityMah, " +
                ":talkTimeHours, :standByTimeHours, :bluetooth, :positioning, :imageUrl, :description)";

    String UPDATE_PHONE = "" +
            "UPDATE phones SET brand = :brand, model = :model, price = :price, displaySizeInches = :displaySizeInches, " +
                "weightGr = :weightGr, lengthMm = :lengthMm, widthMm = :widthMm, heightMm = :heightMm, " +
                "announced = :announced, deviceType = :deviceType, os = :os, displayResolution = :displayResolution, " +
                "pixelDensity = :pixelDensity, displayTechnology = :displayTechnology, backCameraMegapixels = :backCameraMegapixels, " +
                "frontCameraMegapixels = :frontCameraMegapixels, ramGb = :ramGb, internalStorageGb = :internalStorageGb, " +
                "batteryCapacityMah = :batteryCapacityMah, talkTimeHours = :talkTimeHours, standByTimeHours = :standByTimeHours, " +
                "bluetooth = :bluetooth, positioning = :positioning, imageUrl = :imageUrl, description = :description " +
            "WHERE id = :id";

    String DELETE_COLORS_BY_PHONE_ID = "DELETE FROM phone2color WHERE phoneId = ?";

    String INSERT_COLOR_FOR_PHONE_ID = "INSERT INTO phone2color (phoneId, colorId) VALUES (?, ?)";
}
