<template>
  <div>
    <div id="app">
      <v-app id="inspire">
        <v-content>
          <v-container fluid fill-height>
            <v-layout align-center justify-center>
              <v-flex xs12 sm8 md4>
                <v-card class="elevation-12">
                  <v-toolbar dark color="primary">
                    <v-toolbar-title>Music Voting - Adminzugang</v-toolbar-title>
                    <v-spacer></v-spacer>
                  </v-toolbar>
                  <v-card-text>
                    <v-form @submit.prevent="login()">
                      <v-text-field
                        prepend-icon="lock"
                        :append-icon="show ? 'visibility' : 'visibility_off'"
                        @click:append="show = !show"
                        :type="show ? 'text' : 'password'"
                        :rules="[rules.required]"
                        name="password"
                        v-model="password"
                        label="Password"
                        :error-messages="errors"
                        @submit.prevent="login()"
                      ></v-text-field>
                    </v-form>
                  </v-card-text>
                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="primary" @click="login()">Login</v-btn>
                  </v-card-actions>
                </v-card>
              </v-flex>
            </v-layout>
          </v-container>
        </v-content>
      </v-app>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import AuthenticationService from "../services/AuthenticationService";

export default Vue.extend({
  data: () => ({
    password: "",
    show: false,
    errors: [],
    rules: {
      required: value => !!value || "Leere Eingabe nicht möglich."
    }
  }),
  components: {},
  methods: {
    login() {
      AuthenticationService.login(this.password).then(res => {
        if (res.status == 200) this.$router.push(this.$route.query.origin);
        else {
          this.errors = ["Falsches Passwort"];
        }
      });
    }
  }
});
</script>
