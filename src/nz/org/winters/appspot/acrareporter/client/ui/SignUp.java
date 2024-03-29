package nz.org.winters.appspot.acrareporter.client.ui;
/*
 * Copyright 2013 Mathew Winters

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
import nz.org.winters.appspot.acrareporter.client.RemoteDataService;
import nz.org.winters.appspot.acrareporter.client.RemoteDataServiceAsync;
import nz.org.winters.appspot.acrareporter.shared.LoginInfo;
import nz.org.winters.appspot.acrareporter.shared.Utils;
import nz.org.winters.appspot.acrareporter.store.AppUser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class SignUp extends Composite 
{
  @UiField
  TextBox                         textEMailAddress;
  @UiField
  TextBox                         textFirstName;
  @UiField
  TextBox                         textLastName;
  @UiField
  TextBox                         textCity;
  @UiField
  TextBox                         textCountry;
  @UiField
  TextBox                         textAuthUsername;
  @UiField
  PasswordTextBox                 textAuthPassword;
  @UiField
  TextBox                         textTrackingID;
  @UiField
  Button                          buttonOK;
  @UiField
  Button                          buttonCancel;
  @UiField InlineHTML htmlStuff;

  private final RemoteDataServiceAsync remoteService = GWT.create(RemoteDataService.class);
  private UIConstants                   constants     = (UIConstants) GWT.create(UIConstants.class);

  private static SignUpUiBinder uiBinder = GWT.create(SignUpUiBinder.class);
  private LoginInfo loginInfo;

  interface SignUpUiBinder extends UiBinder<Widget, SignUp>
  {
  }
  
  public interface Callback
  {
    void finished();
  };
  private Callback callback;

  public SignUp(LoginInfo loginInfo, Callback callback)
  {
    
    this.callback = callback;
    this.loginInfo = loginInfo;
 
    
    RootLayoutPanel.get().add(uiBinder.createAndBindUi(this));
    textEMailAddress.setText(loginInfo.getEmailAddress());
    textEMailAddress.setReadOnly(true);
    
    htmlStuff.setHTML(constants.signupWibble());
    
    if(loginInfo.getAppUserShared() != null)
    {
      Window.alert(constants.signupAleryAlready());
      callback.finished();
    }
    
  }

  
  @UiHandler("buttonOK")
  void onButtonOKClick(ClickEvent event)
  {
    if(Utils.isEmpty(textFirstName.getText()))
    {
      Window.alert(constants.signupAleryFirstname());
      return;
    }
    if(Utils.isEmpty(textLastName.getText()))
    {
      Window.alert(constants.signupAleryLastname());
      return;
    }
    
    if(Utils.isEmpty(textCity.getText()))
    {
      Window.alert(constants.signupAleryTown());
      return;
    }

    if(Utils.isEmpty(textCountry.getText()))
    {
      Window.alert(constants.signupAleryCountry());
      return;
    }
    
    if(Utils.isEmpty(textAuthUsername.getText()))
    {
      Window.alert(constants.signupAleryAuthUsername());
      return;
    }

    if(Utils.isEmpty(textAuthPassword.getText()))
    {
      Window.alert(constants.signupAleryAuthPassword());
      return;
    }

    AppUser appUserShared = new AppUser();
    
    appUserShared.EMailAddress = textEMailAddress.getText();
    appUserShared.FirstName = textFirstName.getText();
    appUserShared.LastName = textLastName.getText();
    appUserShared.City = textCity.getText();
    appUserShared.Country = textCountry.getText();

    appUserShared.AuthUsername = textAuthUsername.getText();
    appUserShared.AuthPassword = textAuthPassword.getText();

    appUserShared.AnalyticsTrackingId = textTrackingID.getText();

    remoteService.addAppUser(loginInfo, appUserShared, new AsyncCallback<Void>()
    {
      
      @Override
      public void onSuccess(Void result)
      {
        callback.finished();
      }
      
      @Override
      public void onFailure(Throwable caught)
      {
        Window.alert(caught.toString());
        
      }
    });
  }

  @UiHandler("buttonCancel")
  void onButtonCancelClick(ClickEvent event)
  {
    callback.finished();
  }
  
  
 
}
