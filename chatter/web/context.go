package web

import (
	"github.com/npathai/chatter/app"
	"github.com/npathai/chatter/model"
)

type Context struct {
	App *app.App
	Err *model.AppError
}
