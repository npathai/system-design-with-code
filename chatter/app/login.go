package app

import (
	"github.com/npathai/chatter/model"
	"net/http"
)

func (app *App) AuthenticateUserForLogin(userId, loginId, password string) (user *model.User, err *model.AppError) {

	if len(password) == 0 {
		return nil, model.NewAppErrorWithStatus("Password blank", http.StatusBadRequest)
	}

	if user, err = app.GetUserForLogin(userId, loginId); err != nil {
		return nil, err
	}

	if user, err = app.authenticateUser(user, password); err != nil {
		return nil, err
	}

	return user, nil
}

func (app *App) GetUserForLogin(userId, loginId string) (*model.User, *model.AppError){

	// Find user by id and fail if we can't find
	if len(userId) != 0 {
		user, err := app.GetUser(userId)
		if err != nil {
			err.StatusCode = http.StatusBadRequest
			return nil, err
		}
		return user, nil
	}

	// TODO get user by email
	return nil, nil
}

func (app *App) DoLogin(w http.ResponseWriter, r *http.Request, user *model.User) *model.AppError {
	session := &model.Session{
		UserId: user.Id,
	}
	session.GenerateCSRF()
	// TODO add session expiry

	var err *model.AppError
	if session, err = app.CreateSession(session); err != nil {
		err.StatusCode = http.StatusInternalServerError
		return err
	}

	w.Header().Set(model.HeaderToken, session.Token)

	app.SetSession(session)
	return nil
}