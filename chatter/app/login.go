package app

import (
	"github.com/npathai/chatter/model"
	"net/http"
)

func (app *App) AuthenticateUserForLogin(userId, loginId, password string) (user *model.User, err error) {

	if len(password) == 0 {
		return nil, model.NewAppErrorWithStatus("Password blank", http.StatusBadRequest)
	}

	if user, err := app.GetUserForLogin(userId, loginId); err != nil {
		return nil, err
	}


}

func (app *App) GetUserForLogin(userId, loginId string) (*model.User, *model.AppError){

	// Find user by id and fail if we can't find
	if len(userId) != 0 {
		user, err := app.getUser(userId)
		if err != nil {

		}
	}
}
