package app

import (
	"github.com/npathai/chatter/model"
	"net/http"
)

func (app *App) CreateUser(user *model.User) (*model.User, *model.AppError) {
	err := app.validateUser(user)
	if err != nil {
		return nil, model.NewAppErrorWithStatus("Invalid user", http.StatusBadRequest)
	}
	rUser, err := app.Srv().Store.User().Save(user)
	if err != nil {
		return nil, model.NewAppErrorWithStatus("Error creating the user", http.StatusInternalServerError)
	}

	return rUser, nil
}

func (app *App) GetAllUsers() ([]*model.User, *model.AppError) {
	users, err := app.Srv().Store.User().GetAllUsers()
	if err != nil {
		return nil, model.NewAppError("Cannot retrieve users")
	}
	return users, nil
}

func (app *App) validateUser(user *model.User) error {
	// TODO add validation for user
	return nil
}

func (app *App) GetUser(userId string) (*model.User, *model.AppError) {
	user, err := app.Srv().Store.User().Get(userId)
	if err != nil {
		// TODO if error is missing account then return NOT FOUND else return INTERNAL ERROR
		return nil, model.NewAppErrorWithStatus("Account not found", http.StatusNotFound)
	}
	return user, nil
}