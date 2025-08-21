package com.snapmint.merchantsdk.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class EmiModel {

    @SerializedName("pay_now_image1_pop_up_disable")
    private String payNowImage1PopUpDisable;

    @SerializedName("pay_now_text3_pop_up_disable")
    private String payNowText3PopUpDisable;

    @SerializedName("pay_now_text1_part1")
    private String payNowText1Part1;

    @SerializedName("pay_now_text1_part2")
    private String payNowText1Part2;

    @SerializedName("emi_second_percentage")
    private String emiSecondPercentage;

    @SerializedName("pay_now_text1_part3")
    private String payNowText1Part3;

    @SerializedName("pay_now_text1_part4")
    private String payNowText1Part4;

    @SerializedName("terms_and_conditions_subtitle")
    private String termsAndConditionsSubtitle;

    @SerializedName("emi_one_percentage")
    private String emiOnePercentage;

    @SerializedName("emi_one_percentage_3_tenure")
    private String emiOnePercentage3Tenure;

    @SerializedName("emi_second_percentage_3_tenure")
    private String emiSecondPercentage3Tenure;

    @SerializedName("emi_third_percentage_3_tenure")
    private String emiThirdPercentage3Tenure;

    @SerializedName("pay_now_text1_pop_up_disable")
    private String payNowText1PopUpDisable;

    @SerializedName("snapmint_app_name_disable")
    private String snapmintAppNameDisable;

    @SerializedName("pay_now_percentage")
    private String payNowPercentage;

    @SerializedName("pay_now_percentage_3_tenure")
    private String payNowPercentage3Tenure;

    @SerializedName("available_offer_price")
    private String availableOfferPrice;

    @SerializedName("pay_now_text2_pop_up_disable")
    private String payNowText2PopUpDisable;

    @SerializedName("emi_rates_percentage_pop_up_disable")
    private String emiRatesPercentagePopUpDisable;

    @SerializedName("terms_and_conditions_snapmint_logo")
    private String termsAndConditionsSnapmintLogo;

    @SerializedName("pay_now_text4_pop_up_disable")
    private String payNowText4PopUpDisable;

    @SerializedName("offer_percentage")
    private String offerPercentage;

    @SerializedName("available_offer")
    private String availableOffer;

    @SerializedName("offer_terms_and_conditions")
    private List<String> offerTermsAndConditions;

    @SerializedName("terms_and_conditions_title")
    private String termsAndConditionsTitle;

    @SerializedName("pay_now_text3")
    private String payNowText3;

    @SerializedName("pay_now_image_pop_up_disable")
    private String payNowImagePopUpDisable;

    @SerializedName("pay_now_percentage_pop_up_disable")
    private String payNowPercentagePopUpDisable;

    @SerializedName("pay_now_text2")
    private String payNowText2;

    @SerializedName("emi_widget")
    private String emiWidget;

    @SerializedName("emi_pop_up")
    private String emiPopUp;

    @SerializedName("emi_pop_up_1")
    private String emiPopUp1;

    @SerializedName("emi_pop_up_2")
    private String emiPopUp2;

    @SerializedName("emi_pop_up_3")
    private String emiPopUp3;

    @SerializedName("emi_pop_up_4")
    private String emiPopUp4;

    @SerializedName("emi_pop_up_5")
    private String emiPopUp5;

    @SerializedName("tenure_list")
    private List<TenureModel> tenureList;

    @SerializedName("pop_up_list")
    private List<PopUpListItem> popUpList;

    public List<PopUpListItem> getPopUpList() {
        return popUpList;
    }

    public void setPopUpList(List<PopUpListItem> popUpList) {
        this.popUpList = popUpList;
    }

    public String getEmiOnePercentage3Tenure() {
        return emiOnePercentage3Tenure;
    }

    public void setEmiOnePercentage3Tenure(String emiOnePercentage3Tenure) {
        this.emiOnePercentage3Tenure = emiOnePercentage3Tenure;
    }

    public String getEmiSecondPercentage3Tenure() {
        return emiSecondPercentage3Tenure;
    }

    public void setEmiSecondPercentage3Tenure(String emiSecondPercentage3Tenure) {
        this.emiSecondPercentage3Tenure = emiSecondPercentage3Tenure;
    }

    public String getEmiThirdPercentage3Tenure() {
        return emiThirdPercentage3Tenure;
    }

    public void setEmiThirdPercentage3Tenure(String emiThirdPercentage3Tenure) {
        this.emiThirdPercentage3Tenure = emiThirdPercentage3Tenure;
    }

    public String getPayNowPercentage3Tenure() {
        return payNowPercentage3Tenure;
    }

    public void setPayNowPercentage3Tenure(String payNowPercentage3Tenure) {
        this.payNowPercentage3Tenure = payNowPercentage3Tenure;
    }

    public String getEmiPopUp1() {
        return emiPopUp1;
    }

    public void setEmiPopUp1(String emiPopUp1) {
        this.emiPopUp1 = emiPopUp1;
    }

    public String getEmiPopUp2() {
        return emiPopUp2;
    }

    public void setEmiPopUp2(String emiPopUp2) {
        this.emiPopUp2 = emiPopUp2;
    }

    public String getEmiPopUp3() {
        return emiPopUp3;
    }

    public void setEmiPopUp3(String emiPopUp3) {
        this.emiPopUp3 = emiPopUp3;
    }

    public String getEmiPopUp4() {
        return emiPopUp4;
    }

    public void setEmiPopUp4(String emiPopUp4) {
        this.emiPopUp4 = emiPopUp4;
    }

    public String getEmiPopUp5() {
        return emiPopUp5;
    }

    public void setEmiPopUp5(String emiPopUp5) {
        this.emiPopUp5 = emiPopUp5;
    }

    public List<TenureModel> getTenureList() {
        return tenureList;
    }

    public void setTenureList(List<TenureModel> tenureList) {
        this.tenureList = tenureList;
    }

    public String getEmiPopUp() {
        return emiPopUp;
    }

    public void setEmiPopUp(String emiPopUp) {
        this.emiPopUp = emiPopUp;
    }

    public String getEmiWidget() {
        return emiWidget;
    }

    public void setEmiWidget(String emiWidget) {
        this.emiWidget = emiWidget;
    }

    public String getOfferPercentage() {
        return offerPercentage;
    }

    public void setOfferPercentage(String offerPercentage) {
        this.offerPercentage = offerPercentage;
    }

    public String getSnapmintAppNameDisable() {
        return snapmintAppNameDisable;
    }

    public void setPayNowImage1PopUpDisable(String payNowImage1PopUpDisable) {
        this.payNowImage1PopUpDisable = payNowImage1PopUpDisable;
    }

    public String getAvailableOfferPrice() {
        return availableOfferPrice;
    }

    public String getPayNowImage1PopUpDisable() {
        return payNowImage1PopUpDisable;
    }

    public void setPayNowText3PopUpDisable(String payNowText3PopUpDisable) {
        this.payNowText3PopUpDisable = payNowText3PopUpDisable;
    }

    public String getPayNowText3PopUpDisable() {
        return payNowText3PopUpDisable;
    }

    public void setPayNowText1Part1(String payNowText1Part1) {
        this.payNowText1Part1 = payNowText1Part1;
    }

    public String getPayNowText1Part1() {
        return payNowText1Part1;
    }

    public void setPayNowText1Part2(String payNowText1Part2) {
        this.payNowText1Part2 = payNowText1Part2;
    }

    public String getPayNowText1Part2() {
        return payNowText1Part2;
    }

    public void setEmiSecondPercentage(String emiSecondPercentage) {
        this.emiSecondPercentage = emiSecondPercentage;
    }

    public String getEmiSecondPercentage() {
        return emiSecondPercentage;
    }

    public void setPayNowText1Part3(String payNowText1Part3) {
        this.payNowText1Part3 = payNowText1Part3;
    }

    public String getPayNowText1Part3() {
        return payNowText1Part3;
    }

    public void setPayNowText1Part4(String payNowText1Part4) {
        this.payNowText1Part4 = payNowText1Part4;
    }

    public String getPayNowText1Part4() {
        return payNowText1Part4;
    }

    public void setTermsAndConditionsSubtitle(String termsAndConditionsSubtitle) {
        this.termsAndConditionsSubtitle = termsAndConditionsSubtitle;
    }

    public String getTermsAndConditionsSubtitle() {
        return termsAndConditionsSubtitle;
    }

    public void setEmiOnePercentage(String emiOnePercentage) {
        this.emiOnePercentage = emiOnePercentage;
    }

    public String getEmiOnePercentage() {
        return emiOnePercentage;
    }

    public void setPayNowText1PopUpDisable(String payNowText1PopUpDisable) {
        this.payNowText1PopUpDisable = payNowText1PopUpDisable;
    }

    public String getPayNowText1PopUpDisable() {
        return payNowText1PopUpDisable;
    }

    public void setPayNowPercentage(String payNowPercentage) {
        this.payNowPercentage = payNowPercentage;
    }

    public String getPayNowPercentage() {
        return payNowPercentage;
    }

    public void setPayNowText2PopUpDisable(String payNowText2PopUpDisable) {
        this.payNowText2PopUpDisable = payNowText2PopUpDisable;
    }

    public String getPayNowText2PopUpDisable() {
        return payNowText2PopUpDisable;
    }

    public void setEmiRatesPercentagePopUpDisable(String emiRatesPercentagePopUpDisable) {
        this.emiRatesPercentagePopUpDisable = emiRatesPercentagePopUpDisable;
    }

    public String getEmiRatesPercentagePopUpDisable() {
        return emiRatesPercentagePopUpDisable;
    }

    public void setTermsAndConditionsSnapmintLogo(String termsAndConditionsSnapmintLogo) {
        this.termsAndConditionsSnapmintLogo = termsAndConditionsSnapmintLogo;
    }

    public String getTermsAndConditionsSnapmintLogo() {
        return termsAndConditionsSnapmintLogo;
    }

    public void setPayNowText4PopUpDisable(String payNowText4PopUpDisable) {
        this.payNowText4PopUpDisable = payNowText4PopUpDisable;
    }

    public String getPayNowText4PopUpDisable() {
        return payNowText4PopUpDisable;
    }

    public void setAvailableOffer(String availableOffer) {
        this.availableOffer = availableOffer;
    }

    public String getAvailableOffer() {
        return availableOffer;
    }

    public void setOfferTermsAndConditions(List<String> offerTermsAndConditions) {
        this.offerTermsAndConditions = offerTermsAndConditions;
    }

    public List<String> getOfferTermsAndConditions() {
        return offerTermsAndConditions;
    }

    public void setTermsAndConditionsTitle(String termsAndConditionsTitle) {
        this.termsAndConditionsTitle = termsAndConditionsTitle;
    }

    public String getTermsAndConditionsTitle() {
        return termsAndConditionsTitle;
    }

    public void setPayNowText3(String payNowText3) {
        this.payNowText3 = payNowText3;
    }

    public String getPayNowText3() {
        return payNowText3;
    }

    public void setPayNowImagePopUpDisable(String payNowImagePopUpDisable) {
        this.payNowImagePopUpDisable = payNowImagePopUpDisable;
    }

    public String getPayNowImagePopUpDisable() {
        return payNowImagePopUpDisable;
    }

    public void setPayNowPercentagePopUpDisable(String payNowPercentagePopUpDisable) {
        this.payNowPercentagePopUpDisable = payNowPercentagePopUpDisable;
    }

    public String getPayNowPercentagePopUpDisable() {
        return payNowPercentagePopUpDisable;
    }

    public void setPayNowText2(String payNowText2) {
        this.payNowText2 = payNowText2;
    }

    public String getPayNowText2() {
        return payNowText2;
    }
}