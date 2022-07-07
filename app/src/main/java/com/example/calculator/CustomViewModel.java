package com.example.calculator;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CustomViewModel extends ViewModel {
    private MutableLiveData<String> mainText;
    private MutableLiveData<String> operations;
    private MutableLiveData<Double> previousNum;
    private MutableLiveData<Boolean> isDotClicked;
    private MutableLiveData<Boolean> isOperation;
    private MutableLiveData<Boolean> isEqualsClicked;

    public MutableLiveData<String> getMainText() {
        return mainText;
    }

    public MutableLiveData<String> getOperations() {
        return operations;
    }

    public MutableLiveData<Double> getPreviousNum() {
        return previousNum;
    }

    public MutableLiveData<Boolean> getIsDotClicked() {
        return isDotClicked;
    }

    public MutableLiveData<Boolean> getIsOperation() {
        return isOperation;
    }

    public MutableLiveData<Boolean> getIsEqualsClicked() {
        return isEqualsClicked;
    }

    public CustomViewModel()
    {
        mainText = new MutableLiveData<>("0");
        operations = new MutableLiveData<>("");
        previousNum = new MutableLiveData<>((double)0);
        isDotClicked = new MutableLiveData<>(false);
        isOperation = new MutableLiveData<>(false);
        isEqualsClicked = new MutableLiveData<>(false);
    }


    public void setMainText(String mainText) {
        this.mainText.setValue(mainText);
    }

    public void setOperations(String operations) {
        this.operations.setValue(operations);
    }

    public void setPreviousNum(double previousNum) {
        this.previousNum.setValue(previousNum);
    }

    public void setIsDotClicked(boolean isDotClicked) {
        this.isDotClicked.setValue(isDotClicked);
    }

    public void setIsEqualsClicked(boolean isEqualsClicked) {
        this.isEqualsClicked.setValue(isEqualsClicked);
    }

    public void setIsOperation(boolean isOperation)
    {
        this.isOperation.setValue(isOperation);
    }
}
