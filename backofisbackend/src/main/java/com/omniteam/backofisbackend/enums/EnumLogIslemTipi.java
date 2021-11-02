package com.omniteam.backofisbackend.enums;

import lombok.Getter;

@Getter
public enum EnumLogIslemTipi {
   AttributeTermsGet("Attribute Terms Displayed"),//0
   CategoryGetAll("All Categories Viewed"),//1
   CategoryGetById("Category Displayed by Id"), //2
   GetAttributesByAttributeId("Attributes Displayed By Attribute Id"), //3
   GetCountries("Countries Viewed"),
   GetCitiesByCountry("Countries By Cities Displayed"),
   GetDistrictByCity("Cities By Districts Displayed"),
   CustomersGetAll("All Customers Viewed"),
   CustomerAdd("Customer Added"),
   CustomerGetContacts("Customer Concats Recieved"),
   CustomerUpdate("Customer Updated"),
   CustomerGetById("Customer Displayed by Id"),
   CustomerUpdateContacts("Customer Concats Updated"),
   CustomerAddContacts("Customer Concats Added"),
   OrdersGetById("Orders Displayed by Id"),
   OrdersGetAll("All Customers Viewed"),
   GetOrderDetails("Order Details Viewed"),
   OrdersAdd("Orders Added"),
   OrdersUpdate("Orders Updated"),
   OrdersDelete("Orders Deleted"),
   ProductAdd("Product Added"),
   ProductImageAdd("Product Image Added"),
   ProductGetById("Product Displayed By Id"),
   ProductUpdate("Product Updated"),
   ProductGetAll("All Products viewed"),
   UsersGetAll("All User Viewed"),
   UserAdd("User Added"),
   UserUpdate("User Updated"),
   GetAllRoles("All Roles Viewed"),
   GetRoleByUser("Roles By User  Displayed");


   private String value;
   private Integer durum = this.ordinal();

   EnumLogIslemTipi(String value) {
      this.value = value;
   }


   @Override
   public String toString() {
      return this.value;
   }

}
